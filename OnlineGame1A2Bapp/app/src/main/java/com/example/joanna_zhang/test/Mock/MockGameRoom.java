package com.example.joanna_zhang.test.Mock;


import com.example.joanna_zhang.test.Domain.GameMode;
import com.example.joanna_zhang.test.Domain.GameRoom;
import com.example.joanna_zhang.test.Domain.User;

public class MockGameRoom implements GameRoom {

    private String roomName;
    private User roomCreator;
    private GameMode gameMode;
    private int playerAmount;

    public MockGameRoom(String roomName, GameMode gameMode, User roomCreator) {
        this.roomName = roomName;
        this.gameMode = gameMode;
        this.roomCreator = roomCreator;
        playerAmount = 1;
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

    public int getPlayerAmount() {
        return playerAmount;
    }
}
