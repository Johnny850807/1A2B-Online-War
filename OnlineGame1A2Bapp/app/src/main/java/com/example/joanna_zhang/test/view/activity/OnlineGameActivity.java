package com.example.joanna_zhang.test.view.activity;

import android.view.KeyEvent;

import com.example.joanna_zhang.test.R;
import com.example.joanna_zhang.test.Utils.AppDialogFactory;
import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomModel;

import static android.R.string.cancel;
import static com.example.joanna_zhang.test.R.string.confirm;

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
