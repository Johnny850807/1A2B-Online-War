package com.example.joanna_zhang.test.Mock;

import com.example.joanna_zhang.test.Domain.User;

public class MockUser implements User {
    private String name;

    public MockUser(String name){
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return "我是天才";
    }
}
