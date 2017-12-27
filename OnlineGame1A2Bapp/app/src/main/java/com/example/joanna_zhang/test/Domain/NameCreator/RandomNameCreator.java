package com.example.joanna_zhang.test.Domain.NameCreator;

public class RandomNameCreator implements NameCreator {
    String[] names = {"海綿寶寶", "灰太狼", "", "顏秀真", "徐雄健", "王家輝", "蘇民楊", ""};

    @Override
    public String createRandomName() {
        return names[(int) (Math.random() * names.length)];
    }
}
