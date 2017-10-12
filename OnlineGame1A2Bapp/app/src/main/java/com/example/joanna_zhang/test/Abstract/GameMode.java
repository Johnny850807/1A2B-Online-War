package com.example.joanna_zhang.test.Abstract;

public enum GameMode {
    FIGHT(2), DUEL(6);

    private int playerAmount;

    private GameMode(int playerAmount){
        this.playerAmount = playerAmount;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }
}
