package com.techsophy.securitydemo.model;

import java.util.Map;

public class UserData {
    private String id;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, Object> getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(Map<String, Object> customAttributes) {
        this.customAttributes = customAttributes;
    }

    private Map<String, Object> customAttributes;

    @Override
    public String toString() {
        return "UserData{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", customAttributes=" + customAttributes +
                '}';
    }
}
