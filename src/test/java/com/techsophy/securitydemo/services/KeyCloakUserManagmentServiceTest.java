///*
//package com.techsophy.securitydemo.services;
//
//import com.techsophy.securitydemo.model.UserModal;
//import org.junit.Before;
//import org.junit.Test;
//import org.keycloak.admin.client.Keycloak;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class KeyCloakUserManagmentServiceTest {
//
//    private KeyCloakUserManagmentService keyCloakUserManagmentServiceUnderTest;
//
//    @Before
//    public void setUp() {
//        keyCloakUserManagmentServiceUnderTest = new KeyCloakUserManagmentService();
//        keyCloakUserManagmentServiceUnderTest.defaultRoles = List.of();
//        keyCloakUserManagmentServiceUnderTest.serverUrl = "serverUrl";
//        keyCloakUserManagmentServiceUnderTest.realm = "realm";
//        keyCloakUserManagmentServiceUnderTest.AdminClient = "AdminClient";
//        keyCloakUserManagmentServiceUnderTest.keycloak = mock(Keycloak.class);
//    }
//
//    @Test
//    public void testCreateUser() {
//        // Setup
//        final UserModal us = new UserModal("userName", "firstName", "lastName", "password", "eMail", List.of("value"));
//        when(keyCloakUserManagmentServiceUnderTest.keycloak.realm("realmName")).thenReturn(null);
//
//        // Run the test
//        final String result = keyCloakUserManagmentServiceUnderTest.CreateUser(us);
//
//        // Verify the results
//        assertEquals("result", result);
//    }
//
//    @Test
//    public void testGetUserDetails() {
//        // Setup
//        when(keyCloakUserManagmentServiceUnderTest.keycloak.realm("realmName")).thenReturn(null);
//
//        // Run the test
//        final String result = keyCloakUserManagmentServiceUnderTest.getUserDetails("userName", "password");
//
//        // Verify the results
//        assertEquals("result", result);
//    }
//
//    @Test
//    public void testUpdateRoles() {
//        // Setup
//        when(keyCloakUserManagmentServiceUnderTest.keycloak.realm("realmName")).thenReturn(null);
//
//        // Run the test
//        final String result = keyCloakUserManagmentServiceUnderTest.updateRoles("userName", "role");
//
//        // Verify the results
//        assertEquals("result", result);
//    }
//}
//*/
