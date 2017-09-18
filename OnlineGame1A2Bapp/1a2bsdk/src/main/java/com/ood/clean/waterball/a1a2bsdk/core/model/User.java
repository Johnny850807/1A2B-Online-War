package com.ood.clean.waterball.a1a2bsdk.core.model;


import gamecore.entity.BaseEntity;

public class User extends BaseEntity{
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
