package utils;

public class RandomNameCreator {
    private static String[] names = {"Sponge", "Jackson", "Tony", "Sunny", "Java", "Bob", "John", "Emma", "Sans",
    "Chara", "Cindy", "Steve", "Lin", "Tina", "Pica", "Sheng", "ArPing", "Joanna", "Ben", "Nick", "Grace",
    "Ray", "Kate", "Linda", "Vivi", "Rosa", "Yen", "Yin", "Ian", "Jack", "Davis", "Dennis", "Woody", "Julian", "Justin",
    "Deep", "Freeze", "Firen", "Louis", "Coco", "June", "YoJin", "ZongYe", "Water", "Jeff", "Yane", "Shane",
    "Juice", "Kotlin", "Rossa", "Silva", "Coin", "Lora", "Dora", "Diego", "Jessi", "Hook", "Ninja", "Go", "Nay"
    ,"Rey", "Ocean", "Beach", "Todo", "Woody", "Tory"};

    public static String createRandomName() {
        return names[(int) (Math.random() * names.length)];
    }

}
