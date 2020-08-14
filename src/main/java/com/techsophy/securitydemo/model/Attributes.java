package com.techsophy.securitydemo.model;


public class Attributes {


    private String phone;

public String getPhone() {
return phone;
}

public void setPhone(String phone) {
this.phone = phone;
}
    @Override
    public String toString() {
        return "Attributes{" +
                "phone='" + phone + '\'' +
                '}';
    }
}