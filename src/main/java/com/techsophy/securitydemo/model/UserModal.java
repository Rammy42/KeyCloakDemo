package com.techsophy.securitydemo.model;

import java.util.List;

public class UserModal {
    String userName;
    String firstName;
    String lastName;
    String password;
    String eMail;
    List<String> realmRoles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public UserModal(String userName, String firstName, String lastName, String password, String eMail, List<String> realmRoles) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.eMail = eMail;
        this.realmRoles = realmRoles;
    }

    @Override
    public String toString() {
        return "UserModal{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", eMail='" + eMail + '\'' +
                ", realmRoles=" + realmRoles +
                '}';
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }
}
