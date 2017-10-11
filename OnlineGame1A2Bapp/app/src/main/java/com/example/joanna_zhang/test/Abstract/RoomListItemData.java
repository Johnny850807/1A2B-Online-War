package com.example.joanna_zhang.test.Abstract;

public abstract class RoomListItemData {

    private String roomName;
    private String roomCreatorName;
    private Mode mode;
    private int peopleAmount;

    public String getRoomName() {
        return roomName;
    }

    public String getRoomCreatorName() {
        return roomCreatorName;
    }

    public Mode getMode() {
        return mode;
    }

    public int getPeopleAmount() {
        return peopleAmount;
    }
}
