package com.ood.clean.waterball.a1a2bsdk.core.model;


public class User extends SerializableEntity{

    private String name;

    public User(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
