package com.techsophy.securitydemo.model;

import java.util.List;

public class KeyCloakUserPayLoad {

private String username;
private Boolean enabled;
private Boolean emailVerified;
private String firstName;
private String lastName;

    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    private Attributes attributes;
private List<Credential> credentials = null;
private List<String> realmRoles;

public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}

public Boolean getEnabled() {
return enabled;
}

public void setEnabled(Boolean enabled) {
this.enabled = enabled;
}

public Boolean getEmailVerified() {
return emailVerified;
}

public void setEmailVerified(Boolean emailVerified) {
this.emailVerified = emailVerified;
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

public Attributes getAttributes() {
return attributes;
}

public void setAttributes(Attributes attributes) {
this.attributes = attributes;
}

public List<Credential> getCredentials() {
return credentials;
}

public void setCredentials(List<Credential> credentials) {
this.credentials = credentials;
}

    @Override
    public String toString() {
        return "KeyCloakUserPayLoad{" +
                "username='" + username + '\'' +
                ", enabled=" + enabled +
                ", emailVerified=" + emailVerified +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", attributes=" + attributes +
                ", credentials=" + credentials +
                ", roles=" + realmRoles +
                '}';
    }
}