package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.ChatModuleImp;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerStatus;


public class InRoomModuleImp extends ChatModuleImp implements InRoomModule{
    protected @Inject EventBus eventBus;
    protected @Inject Client client;
    protected @Inject ProtocolFactory protocolFactory;
    protected Gson gson = new Gson();
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
        public void onNewPlayer(PlayerStatus playerStatus) {
            callback.onNewPlayer(playerStatus);
        }

        @Override
        public void onPlayerStatusChanged(PlayerStatus playerStatus) {
            callback.onPlayerStatusChanged(playerStatus);
        }

        @Override
        public void onPlayerLeft(PlayerStatus playerStatus) {
            callback.onPlayerLeft(playerStatus);
        }

        @Override
        public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
            callback.onGameLaunchedSuccessfully(gameRoom);
        }

        @Override
        public void onGameLaunchedFailed() {
            callback.onGameLaunchedFailed();
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
