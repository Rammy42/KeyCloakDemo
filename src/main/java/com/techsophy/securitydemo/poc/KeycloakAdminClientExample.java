package com.techsophy.securitydemo.poc;

import com.techsophy.securitydemo.model.UserModal;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class KeycloakAdminClientExample {

    public  static  org.keycloak.representations.idm.RoleRepresentation getRealmRole(String role,RealmResource realmResource)
    {
        return  realmResource.roles()//
                .get(role).toRepresentation();
    }
    public  static  org.keycloak.representations.idm.RoleRepresentation getClientRole(String role,RealmResource realmResource, ClientRepresentation app1Client)
    {
        return  realmResource.roles()//
                .get(role).toRepresentation();
    }
    public static void main(String[] args) {


        UserModal us = new UserModal(
                "ramana",
                "chintha",
                "venkata",
                "password",
                "ramanagioe@gmail.com",
                List.of("app-admin")
        );

        String serverUrl = "http://localhost:8080/auth";
        String realm = "Demo-Realm";
        String userName = "emp1";
        var password ="mypassword";
        var Adminclient="admin-cli";


        // User "idm-admin" needs at least "manage-users, view-clients, view-realm, view-users" roles for "realm-management"

        Keycloak keycloak = KeycloakBuilder.builder() //
                .serverUrl(serverUrl) //
                .realm(realm)//
                .username(userName) //
                .password(password) //
                .clientId(Adminclient) //
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
                .build();
        // Define user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(us.getUserName());
        user.setFirstName(us.getFirstName());
        user.setLastName(us.getLastName());
        user.setEmail(us.geteMail());
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

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
        passwordCred.setValue(us.getPassword());

        UserResource userResource = usersRessource.get(userId);

        // Set password credential
        userResource.resetPassword(passwordCred);
        var realmRolesList= us.getRealmRoles().stream().map(s -> getRealmRole(s,realmResource)).collect(toList());
        userResource.roles().realmLevel() //
                .add(realmRolesList);




    }
}