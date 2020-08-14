package com.techsophy.securitydemo.controller;
import com.techsophy.securitydemo.model.KeyCloakUserPayLoad;

import com.techsophy.securitydemo.model.RolesRep;
import com.techsophy.securitydemo.services.KeyCloakRestApiImpl;
import org.json.JSONObject;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class KeyCloakUserMangmentController {

    @Autowired
    KeyCloakRestApiImpl keyCloakRestApi;

    @RequestMapping(value = "/anonymous", method = RequestMethod.GET)
    public ResponseEntity<String> getAnonymous() {
        return ResponseEntity.ok("Hello Anonymous");
    }

    @RolesAllowed("user")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<String> getUser(HttpServletRequest request) {

        @SuppressWarnings("rawtypes")
        AccessToken token = ((KeycloakPrincipal) request.getUserPrincipal()).getKeycloakSecurityContext().getToken();
        var attr = token.getOtherClaims();
        return ResponseEntity.ok("Hello User  " + attr.toString());
    }

    @RolesAllowed("admin")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Hello admin");
    }

    @RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public ResponseEntity<String> getAllUser() {
        return ResponseEntity.ok("Hello all user");
    }

    @RolesAllowed("admin")
    @GetMapping({"get-data"})
    public String test(HttpServletRequest request) {
        Map<String, String> op = new HashMap<>();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        HttpSession reqSes = request.getSession();
        System.out.println(reqSes.getAttribute("attributeKey"));

        String username = accessToken.getPreferredUsername();
        op.put("username", username);
        String emailID = accessToken.getEmail();

        String lastname = accessToken.getFamilyName();
        String firstname = accessToken.getGivenName();
        String realmName = accessToken.getIssuer();
        Map<String, Object> attributes = accessToken.getOtherClaims();
        System.out.println(firstname);
        String mobileNumber = (String) attributes.get("phone");
        var country = attributes.getOrDefault("Country","No Val");
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        Set<String> roles = realmAccess.getRoles();
        op.put("attributes",attributes.toString());
        op.put("emailID", emailID);
        op.put("UserId", accessToken.getId());
        op.put("firstname", firstname);
        op.put("Country", country.toString());
        op.put("mobileNumber", mobileNumber);
        return op.toString();
    }

    @RolesAllowed("admin")
    @PostMapping({"create-user"})
    public String createUser(HttpServletRequest request, Principal principal2, @RequestHeader Map<String, String> headers, HttpServletResponse response, @RequestBody KeyCloakUserPayLoad keyCloakUserPayLoad) {

        JSONObject userDetails = null;
        try {
            List<RolesRep> clientROles = null;
            List<JSONObject> appliedROles = null;
            var cookieData = getAdminAccessToken(request, response);
            boolean userCreation = keyCloakRestApi.createKeyCloakUser(keyCloakUserPayLoad, cookieData);
            String userId = "";

            if (userCreation) {

                userDetails = keyCloakRestApi.getUserDetails(cookieData).orElseThrow(() -> new Exception("Failed to Fetch user Details"));
                userId = userDetails.getString("id");
                clientROles = keyCloakRestApi.getClientRoles(cookieData);
                keyCloakRestApi.mapClientROles(cookieData, userId, getAppliedRoles(keyCloakUserPayLoad.getRealmRoles(), clientROles));

            }
            return clientROles.size() > 0 ? "User Created successfully" + userDetails.toString() : "User Creation Failed";

        } catch (Exception e) {
            return "error" + e;
        }
    }

    private List<RolesRep> getAppliedRoles(List<String> toBeAppliedRoles, List<RolesRep> availableRoles) throws Exception {
        System.out.println("In filter");
        System.out.println("toBeAppliedRoles" +toBeAppliedRoles.stream().collect(Collectors.joining("|")));
        var data = availableRoles.stream().filter(s -> toBeAppliedRoles.contains(s.getName())).collect(Collectors.toList());
        if (data.isEmpty()) throw new Exception("No Applicable ROles Available");
        return data;
    }

    private String getAdminAccessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        var cookie = Arrays.stream(request.getCookies())
                .filter(c -> "Testing".equals(c.getName()))
                .map(Cookie::getValue)
                .findAny();

        var cookieData = cookie.orElse(refreshCookies(response));
        System.out.println("Testing Data " + cookieData);
        return cookieData;
    }

    private String refreshCookies(HttpServletResponse response) throws Exception {
        var data = keyCloakRestApi.refreshAccessToken().orElseThrow(() -> new Exception("Unable to retrive Access Toekn"));
        var accessToken = data.getString("access_token");
        Cookie uiColorCookie = new Cookie("Testing", accessToken);
        uiColorCookie.setMaxAge(data.getInt("expires_in"));
        System.out.println("refreshCookies Ok");
        response.addCookie(uiColorCookie);
        return accessToken;
    }

}