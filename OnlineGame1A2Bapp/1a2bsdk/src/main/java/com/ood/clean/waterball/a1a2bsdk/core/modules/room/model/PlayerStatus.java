package com.ood.clean.waterball.a1a2bsdk.core.modules.room.model;

import com.ood.clean.waterball.a1a2bsdk.core.model.Player;

public enum PlayerStatus implements IPlayerStatus{
    NONE,
    READY,
    UNREADY;

    private Player player;
    private boolean ready;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isReady() {
        return ready;
    }


    public void setReady(boolean ready) {
        this.ready = ready;
    }

    //setter&getter來設玩家以及取的玩家資訊
    //玩家要傳進來

}
