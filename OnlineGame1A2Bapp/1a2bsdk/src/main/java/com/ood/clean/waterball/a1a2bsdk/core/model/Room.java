package com.ood.clean.waterball.a1a2bsdk.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gamecore.entity.BaseEntity;


public class Room extends BaseEntity {
    private List<User> userList = Collections.checkedList(new ArrayList<User>(), User.class);
    private String name;
    private int limitOfPlayer;
}
