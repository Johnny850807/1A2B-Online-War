package com.example.joanna_zhang.test.Domain.NameCreator;

public class RandomNameCreator implements NameCreator {
    String[] names = {"Sponge", "Jackson", "Tony", "Sunny", "Java", "Bob", "John", "Emma", "Sans",
    "Chara", "Cindy", "Steve", "Lin", "Tina", "Pica", "Sheng", "ArPing", "Joanna", "Ben", "Nick", "Grace",
    "Ray", "Kate", "Linda", "Vivi", "Rosa", "Yen", "Yin", "Ian", "Jack", "Davis", "Dennis", "Woody", "Julian", "Justin",
    "Deep", "Freeze", "Firen", "Louis", "Coco", "June", "YoJin", "ZongYe", "Water", "Jeff", "Yane"};

    @Override
    public String createRandomName() {
        return names[(int) (Math.random() * names.length)];
    }
}
