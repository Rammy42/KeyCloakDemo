package com.techsophy.securitydemo.model;

public class Credential {

private String type;
private String value;
private Boolean temporary;

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getValue() {
return value;
}

public void setValue(String value) {
this.value = value;
}

public Boolean getTemporary() {
return temporary;
}

public void setTemporary(Boolean temporary) {
this.temporary = temporary;
}

    @Override
    public String toString() {
        return "Credential{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", temporary=" + temporary +
                '}';
    }
}