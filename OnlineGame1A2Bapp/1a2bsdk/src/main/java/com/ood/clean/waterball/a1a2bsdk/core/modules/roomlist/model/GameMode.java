package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model;


public enum GameMode implements IGameMode {
    DUEL1A2B(2,2),
    GROUP1A2B(2,6),
    DIXIT(3,6);

    private int minPlayerAmount;
    private int maxPlayerAmount;

    GameMode(int minPlayerAmount, int maxPlayerAmount) {
        this.minPlayerAmount = minPlayerAmount;
        this.maxPlayerAmount = maxPlayerAmount;
    }

    @Override
    public int getMinPlayerAmount() {
        return minPlayerAmount;
    }

    public int getMaxPlayerAmount(){
        return maxPlayerAmount;
    }

}
