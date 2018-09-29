package com.example.joanna_zhang.test.view.activity;

import com.example.joanna_zhang.test.view.dialog.AppDialogFactory;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomModel;

public abstract class OnlineGameActivity extends BaseAbstractActivity implements OnlineGameModule.Callback{

    @Override
    public void onRoomExpired() {
        AppDialogFactory.roomTimeExpiredDialog(this);
    }

    @Override
    public void onPlayerLeft(PlayerRoomModel model) {
        AppDialogFactory.playerLeftFromGameDialog(this, model.getPlayer()).show();
    }

    @Override
    public void onGameClosed(GameRoom gameRoom) {
        AppDialogFactory.playerLeftFromGameDialog(this, gameRoom.getHost()).show();
    }
}
