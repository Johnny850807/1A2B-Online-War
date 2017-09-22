package com.example.joanna_zhang.test.Game;

import com.example.joanna_zhang.test.NameCreator.NameCreator;



public class RandomNameCreator implements NameCreator {

    private String[] names = {"莫載啼", "莫載妏", "遊戲王", "林筱珍", "包龍興", "橙汁內", "墨 海馬"};

    @Override
    public String createRandomName() {
        String name = names[(int) (Math.random() * names.length)];

        return name;
    }
}
