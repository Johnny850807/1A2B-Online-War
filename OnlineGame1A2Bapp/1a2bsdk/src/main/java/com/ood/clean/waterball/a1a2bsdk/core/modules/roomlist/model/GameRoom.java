package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model;

import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.model.SerializableEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameRoom extends SerializableEntity {
    private Player roomHost;
    private List<Player> playerList = Collections.checkedList(new ArrayList<Player>(), Player.class);
    private String name;
    private GameMode gameMode;
    private boolean playing = false;

    public GameRoom() {}

    public GameRoom(String name, GameMode gameMode, Player roomHost) {
        this.name = name;
        this.gameMode = gameMode;
        this.roomHost = roomHost;
    }

    public Player getRoomHost() {
        return roomHost;
    }

    public void setRoomHost(Player roomHost) {
        this.roomHost = roomHost;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPlayerAmount() {
        return gameMode.getMaxPlayerAmount();
    }


    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getMinPlayerAmount() {
        return gameMode.getMinPlayerAmount();
    }


    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
