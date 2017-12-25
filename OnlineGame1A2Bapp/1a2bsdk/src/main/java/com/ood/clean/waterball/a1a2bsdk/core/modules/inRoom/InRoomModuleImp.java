package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerStatus;
import gamecore.model.RequestStatus;

import static container.Constants.Events.InRoom.CHANGE_STATUS;
import static container.Constants.Events.InRoom.LAUNCH_GAME;
import static container.Constants.Events.InRoom.LEAVE_ROOM;
import static container.Constants.Events.RoomList.JOIN_ROOM;


public class InRoomModuleImp extends AbstractGameModule implements InRoomModule{
    protected ProxyCallback proxyCallback;

    @Override
    public void registerCallback(InRoomModule.Callback callback) {
        if (this.proxyCallback != null)
            callback.onError(new CallbackException());
        this.proxyCallback = new InRoomModuleImp.ProxyCallback(callback);
        eventBus.registerCallback(proxyCallback);
    }

    @Override
    public void unregisterCallBack(InRoomModule.Callback callback) {
        if (this.proxyCallback == null || this.proxyCallback.callback != callback)
            callback.onError(new CallbackException());
        eventBus.unregisterCallback(proxyCallback);
        this.proxyCallback = null;
    }

    @Override
    public void changeStatus(boolean prepare) {

    }

    @Override
    public void launchGame() {

    }

    @Override
    public void bootPlayer(Player player) {

    }

    @Override
    public void closeRoom() {

    }

    public class ProxyCallback implements InRoomModule.Callback{
        private InRoomModule.Callback callback;

        public ProxyCallback(InRoomModule.Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = JOIN_ROOM, status = RequestStatus.success)
        public void onPlayerJoined(PlayerStatus playerStatus) {
            Log.d(TAG, "player " + playerStatus.getPlayer().getName() + " joined.");
            callback.onPlayerJoined(playerStatus);
        }

        @Override
        @BindCallback(event = CHANGE_STATUS, status = RequestStatus.success)
        public void onPlayerStatusChanged(PlayerStatus playerStatus) {
            Log.d(TAG, "player " + playerStatus.getPlayer().getName() + "'s status changed, ready:" + playerStatus.isReady() +".");
            callback.onPlayerStatusChanged(playerStatus);
        }

        @Override
        @BindCallback(event = LEAVE_ROOM, status = RequestStatus.success)
        public void onPlayerLeft(PlayerStatus playerStatus) {
            Log.d(TAG, "player " + playerStatus.getPlayer().getName() + " left.");
            callback.onPlayerLeft(playerStatus);
        }

        @Override
        @BindCallback(event = LAUNCH_GAME, status = RequestStatus.success)
        public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
            Log.d(TAG, "Game launched, info: " + gameRoom);
            callback.onGameLaunchedSuccessfully(gameRoom);
        }

        @Override
        @BindCallback(event = LAUNCH_GAME, status = RequestStatus.failed)
        public void onGameLaunchedFailed(GameRoom gameRoom) {
            Log.d(TAG, "Game launched, info: " + gameRoom);
            callback.onGameLaunchedFailed(gameRoom);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
