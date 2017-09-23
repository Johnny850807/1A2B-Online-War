package com.ood.clean.waterball.a1a2bsdk.core.model;



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

    public void setOnlineAmount(int onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public int getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(int roomAmount) {
        this.roomAmount = roomAmount;
    }
}
