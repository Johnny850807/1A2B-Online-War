package com.example.joanna_zhang.test.view.activity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.GameOverModel;

public class OnlineGameActivity extends BaseAbstractActivity implements OnlineGameModule.Callback{
    @Override
    public void onRoomExpired() {
        AppDialogFactory.roomTimeExpiredDialog(this);
    }

    @Override
    public void onServerReconnected() {

    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    @Override
    public void onGameOver(GameOverModel gameOverModel) {

    }

    public void onPlayerLeft(PlayerRoomModel model) {
        AppDialogFactory.playerLeftFromGameDialog(this, model.getPlayer()).show();
    }

    public void onGameClosed(GameRoom gameRoom) {
        AppDialogFactory.playerLeftFromGameDialog(this, gameRoom.getHost()).show();
    }
}
