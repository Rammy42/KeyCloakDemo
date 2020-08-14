package com.techsophy.securitydemo.controller;

import com.techsophy.securitydemo.model.UserModal;
import com.techsophy.securitydemo.services.KeyCloakUserManagmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class keyCloakUserManagementController {
@Autowired
private KeyCloakUserManagmentService keyCloakUserManagmentService;
    @PostMapping("/create-user")
    public String createUser(@RequestBody UserModal us)
{
return  keyCloakUserManagmentService.CreateUser(us);
}

@GetMapping("/update-password")
    public  String getUserDetails(@RequestParam String  userName, @RequestParam String password)
{
    return  keyCloakUserManagmentService.getUserDetails(userName,password);
}

@GetMapping("/update-roles")
public  String getUserRoles(@RequestParam String  userName, @RequestParam String roles)
{
    return  keyCloakUserManagmentService.updateRoles(userName,roles);
}
}