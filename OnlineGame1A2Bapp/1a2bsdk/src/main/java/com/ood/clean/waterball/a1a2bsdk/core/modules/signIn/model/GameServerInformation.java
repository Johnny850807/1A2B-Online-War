package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.model;



public class GameServerInformation {
    private int onlineAmount;
    private int roomAmount;

    public GameServerInformation(int onlineAmount, int roomAmount) {
        this.onlineAmount = onlineAmount;
        this.roomAmount = roomAmount;
    }

    public int getOnlineAmount() {
        return onlineAmount;
    }

    public int getRoomAmount() {
        return roomAmount;
    }
}
