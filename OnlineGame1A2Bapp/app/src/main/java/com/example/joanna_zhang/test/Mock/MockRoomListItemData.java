package com.example.joanna_zhang.test.Mock;


import com.example.joanna_zhang.test.Abstract.GameMode;
import com.example.joanna_zhang.test.Abstract.RoomListItemData;
import com.example.joanna_zhang.test.Abstract.User;

public class MockRoomListItemData extends RoomListItemData {

    private String roomName;
    private User roomCreator;
    private GameMode gameMode;
    private int peopleAmount;

    public MockRoomListItemData(String roomName, GameMode gameMode, User roomCreator) {
        this.roomName = roomName;
        this.gameMode = gameMode;
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

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public int getPeopleAmount() {
        return peopleAmount;
    }
}
