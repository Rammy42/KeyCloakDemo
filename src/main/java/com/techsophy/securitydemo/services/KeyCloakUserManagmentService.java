package com.techsophy.securitydemo.services;

import com.techsophy.securitydemo.model.UserModal;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONObject;
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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.ws.rs.core.Response;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class KeyCloakUserManagmentService {

    private static final String userName = "emp1";
    private static final String password ="mypassword";
    List defaultRoles = List.of("offline_access","uma_authorization");
    String serverUrl = "http://localhost:8080/auth";
    String realm = "Demo-Realm";

    String AdminClient="admin-cli";
    Keycloak keycloak = KeycloakBuilder.builder() //
            .serverUrl(serverUrl) //
            .realm(realm)//
            .username(userName) //
            .password(password) //
            .clientId(AdminClient) //
            .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
            .build();

    public String CreateUser(UserModal us)
    {
        String userCreationStatus = "User Creation Failed";

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

        if(response.getStatus() ==201)
        {
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
            var realmRolesList= us.getRealmRoles().stream().map(s -> getRealmRole(s.trim().toLowerCase(),realmResource)).filter(Objects::nonNull).collect(toList());

            if(! realmRolesList.isEmpty()) {
                userResource.roles().realmLevel() //
                        .add(realmRolesList);
                userCreationStatus = "User created with userId:"+ userId +" With Following Roles : "+ Arrays.toString(realmRolesList.toArray());
            }
            else {
                userCreationStatus = "User created with userId:"+ userId +" Without any Role Mapping";
            }
        }
        else if(response.getStatus() ==409)
        {
            userCreationStatus = "User Already Exists";
        }
        return userCreationStatus;
    }
    private RoleRepresentation getRealmRole(String role,RealmResource realmResource)
    {
        RoleRepresentation roleRep =null;
        try
        {
            roleRep =  realmResource.roles()//
                    .get(role).toRepresentation();
        }
        catch (Exception e)
        {
            System.out.println("Error in Getting Role Rep"+e);
        }
        return roleRep;

    }

    /**
     * Update Password for the user
     * @param userName username to which password will be updated
     * @param password new Password
     * @return
     */
    public String getUserDetails(String userName,String password)
    {
        String status = "No User Exist";
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        var users = usersResource.search(userName);
        if(!users.isEmpty())
        {
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(password);
            usersResource.get(users.get(0).getId()).resetPassword(passwordCred);
            status = "Password updated Successfully";
        }
        else
        {
            System.out.println("No User Found With User Id");
        }
        return status;
    }


    public String updateRoles(String userName,String role)
    {
        String status = "No User Found";
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        var users = usersResource.search(userName);
        if(!users.isEmpty())
        {

            var realmRemoveRolesList= realmResource.roles().list().stream().filter( s-> !defaultRoles.contains(s.toString().trim())).collect(toList());
            if(! realmRemoveRolesList.isEmpty())
            {
                System.out.println("Roles to remove" +Arrays.toString(realmRemoveRolesList.toArray()));
                usersResource.get(users.get(0).getId()).roles().realmLevel().remove(realmRemoveRolesList);
            }

            var realmRolesList= List.of(role.trim().split(",")).stream().map(s -> getRealmRole(s.trim().toLowerCase(),realmResource)).filter(Objects::nonNull).collect(toList());
            if(! realmRolesList.isEmpty())
            {
                usersResource.get(users.get(0).getId()).roles().realmLevel().add(realmRolesList);
                status = "User Updated with following roles"+ Arrays.toString(realmRolesList.toArray());
            }
            else
            {
                status = "User Updated with out any role";
            }
        }
        else
        {
            System.out.println("No User Found With User Id");
        }

        return status;
    }


}
