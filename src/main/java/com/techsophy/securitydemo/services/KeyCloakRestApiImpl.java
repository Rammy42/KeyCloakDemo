package com.techsophy.securitydemo.services;

import com.techsophy.securitydemo.model.KeyCloakUserPayLoad;
import com.techsophy.securitydemo.model.RolesRep;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class KeyCloakRestApiImpl {
    private static final String GRANT_TYPE = "grant_type";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_URL_ENCODE = "application/x-www-form-urlencoded";
    private static final String CLIENT_ID = "client_id";
    private static final String userName = "emp1";
    private static final String url = "localhost:8080/auth/realms/Demo-Realm/protocol/openid-connect/token";
    private static final String password ="web@123";
    private final WebClient webClient;

    @Value("${kc_role_mapping_url}")
    private String roleMappingURL;

    @Value("${kcroles_url}")
    private String userCreationUrl;
    @Value("${kc_client_id}")
    private String kcClientId;
    public KeyCloakRestApiImpl(@Value("${keycloak.auth-server-url}") String baseURL) {
        this.webClient = WebClient.builder().baseUrl(baseURL)
                .build();
    }

    public boolean createKeyCloakUser(KeyCloakUserPayLoad payload, String token) {
        try {
            System.out.println(payload.toString());
            String res = this.webClient.post()
                    .uri("/admin/realms/Demo-Realm/users")
                    .header("Content-Type", "application/json")
                    .headers(h -> h.setBearerAuth(token))
                    .body(Mono.just(payload), KeyCloakUserPayLoad.class)
                    .retrieve()
                    .bodyToMono(String.class).block();
            System.out.println("res" + res);
            System.out.println("User Created Done");
        } catch (WebClientResponseException webClientResponseException) {
            System.out.println(webClientResponseException.getResponseBodyAsString());
            webClientResponseException.printStackTrace();
            throw webClientResponseException;
        } catch (Exception exception) {
            throw exception;
        }
        return true;
    }




    public Optional<String> mapClientROles(String token, String userId, List<RolesRep> rolesRep)
    {
        String response =null;
        try {


                 response =this.webClient.put()
                    .uri( MessageFormat.format(roleMappingURL,userId,kcClientId))
                    .headers(h ->h.setBearerAuth(token) )
                         .body(Flux.just(rolesRep.toArray()),RolesRep.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


        System.out.println("ROles Applied DOne!");
        } catch (WebClientResponseException ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }
        return Optional.ofNullable(response);
    }

    public List<RolesRep> getClientRoles(String token) throws Exception {
        List<RolesRep> rolesList=null;
        try {

            rolesList= this.webClient.get()
                    .uri( MessageFormat.format(userCreationUrl,kcClientId))
                    .headers(h ->h.setBearerAuth(token) )
                    .retrieve()
                    .bodyToFlux(RolesRep.class)
                    .collectList().block();
           if (rolesList.size() == 0)
           {
               System.out.println("No roles Found");
               throw new Exception("No roles found");
           }

        } catch (WebClientResponseException ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }


        return rolesList;
    }
    public Optional<JSONObject> getUserDetails(String token)
    {
        JSONObject jsonResponse =null;
        try {


            String response =this.webClient.get()
                    .uri(builder -> builder.path("/admin/realms/Demo-Realm/users").queryParam("username", "nameRest").build())
                    .headers(h ->h.setBearerAuth(token) )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("User Creation : " + response);
            JSONArray js = new JSONArray(response);
            jsonResponse = (JSONObject) js.get(0);

        } catch (WebClientResponseException ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }
        return Optional.ofNullable(jsonResponse);
    }
    public Optional<JSONObject> refreshAccessToken() {

        JSONObject jsonResponse =null;
        try {

            WebClient client1 = WebClient.builder()
                    .defaultHeader(CONTENT_TYPE, CONTENT_TYPE_URL_ENCODE)
                    .build();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(GRANT_TYPE, "password");
            map.add(CLIENT_ID, "admin-cli");
            map.add("username", userName);
            map.add("password", password);
            String response = client1.post()
                    .uri(url)
                    .body(BodyInserters.fromFormData(map))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("response: " + response);
            jsonResponse = new JSONObject(response);

        } catch (WebClientResponseException ex) {
            throw ex;

        } catch (Exception e) {
            throw e;
        }
        return Optional.ofNullable(jsonResponse);
    }
}
