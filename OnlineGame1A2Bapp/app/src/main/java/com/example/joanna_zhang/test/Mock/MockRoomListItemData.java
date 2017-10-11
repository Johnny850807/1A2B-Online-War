package com.example.joanna_zhang.test.Mock;


import com.example.joanna_zhang.test.Abstract.Mode;
import com.example.joanna_zhang.test.Abstract.RoomListItemData;
import com.example.joanna_zhang.test.Abstract.User;

public class MockRoomListItemData extends RoomListItemData {

    private String roomName;
    private User roomCreator;
    private Mode mode;
    private int peopleAmount;

    public MockRoomListItemData(String roomName, Mode mode, User roomCreator) {
        this.roomName = roomName;
        this.mode = mode;
        this.roomCreator = roomCreator;
        peopleAmount = 1;
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    @Override
    public String getRoomCreatorName() {
        return roomCreator.getName();
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public int getPeopleAmount() {
        return peopleAmount;
    }
}
