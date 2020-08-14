package com.techsophy.securitydemo.poc;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

public class KeycloakAdminClientExampleOld {

    public  static  RoleRepresentation getRole(String role,RealmResource realmResource)
    {
        return  realmResource.roles()//
                .get(role).toRepresentation();
    }
    public static void main(String[] args) {

        String serverUrl = "http://localhost:8080/auth";
        String realm = "Demo-Realm";
        String userName = "emp1";
        var password ="mypassword";
        var client="admin-cli";


        // User "idm-admin" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"

        Keycloak keycloak = KeycloakBuilder.builder() //
                .serverUrl(serverUrl) //
                .realm(realm)//
                .username(userName) //
                .password(password) //
                .clientId(client) //
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
                .build();
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername("tester11");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("tom+tester11@tdlabs.local");
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));
        user.setAttributes(Collections.singletonMap("Country", Arrays.asList("India")));
        user.setAttributes(Collections.singletonMap("VSC", Arrays.asList("HYD")));
        user.setAttributes(Collections.singletonMap("IsActive", Arrays.asList("True")));
        Map<String,List<String>> mapAttr = new HashMap<>();
        mapAttr.put("phone",Arrays.asList("9868787873"));
        mapAttr.put("Country",Arrays.asList("India"));
        mapAttr.put("VSC",Arrays.asList("HYd"));
        mapAttr.put("IsActive",Arrays.asList("True"));

        user.setAttributes(mapAttr);

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersRessource = realmResource.users();

        // Create user (requires manage-users role)
        Response response = usersRessource.create(user);
        System.out.printf("Repsonse: %s %s%n", response.getStatus(), response.getStatusInfo());
        System.out.println(response.getLocation());
        String userId = CreatedResponseUtil.getCreatedId(response);

        System.out.printf("User created with userId: %s%n", userId);

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue("test");

        UserResource userResource = usersRessource.get(userId);

        // Set password credential
        userResource.resetPassword(passwordCred);

//        // Get realm role "tester" (requires view-realm role)
        var realmRoles = List.of("app-admin","app-user");
       var realmRolesList= realmRoles.stream().map(s -> getRole(s,realmResource)).collect(Collectors.toList());

        RoleRepresentation testerRealmRole = realmResource.roles()//
                .get("app-admin").toRepresentation();
//
//        // Assign realm role tester to user
        userResource.roles().realmLevel() //
                .add(realmRolesList);
//
//        // Get client
        ClientRepresentation app1Client = realmResource.clients() //
                .findByClientId("DemoApp").get(0);
//
//        // Get client level role (requires view-clients role)
        RoleRepresentation userClientRole = realmResource.clients().get(app1Client.getId()) //
                .roles().get("admin").toRepresentation();
//
//        // Assign client level role to user
        userResource.roles() //
                .clientLevel(app1Client.getId()).add(Arrays.asList(userClientRole));
        System.out.println("removing user");


        // Send password reset E-Mail
        // VERIFY_EMAIL, UPDATE_PROFILE, CONFIGURE_TOTP, UPDATE_PASSWORD, TERMS_AND_CONDITIONS
//        usersRessource.get(userId).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));

        // Delete User
//        userResource.remove();
    }
}