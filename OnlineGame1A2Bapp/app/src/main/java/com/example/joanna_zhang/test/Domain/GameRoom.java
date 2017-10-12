package com.example.joanna_zhang.test.Domain;

public interface GameRoom {
    String getRoomName();
    String getRoomCreatorName();
    GameMode getGameMode();
    int getPlayerAmount();
}
