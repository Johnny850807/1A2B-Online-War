package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.List;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.GameRoom;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;


public class RoomListModuleImp implements RoomListModule {
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private Gson gson = new Gson();
    private UserSigningModule signingModule;
    private ProxyCallback proxyCallback;

    public RoomListModuleImp() {
        Component.inject(this);
        this.signingModule = (UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING);
    }

    @Override
    public void registerCallback(Callback callback) {
        if (this.proxyCallback != null)
            callback.onError(new CallbackException());
        this.proxyCallback = new ProxyCallback(callback);
        eventBus.registerCallback(proxyCallback);
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        if (this.proxyCallback == null || this.proxyCallback.callback != callback)
            callback.onError(new CallbackException());
        eventBus.unregisterCallback(proxyCallback);
        this.proxyCallback = null;
    }

    @Override
    public void createRoom(String roomName, GameMode gameMode) {
        GameRoom gameRoom = new GameRoom(gameMode, roomName, signingModule.getCurrentPlayer());
        Protocol protocol = protocolFactory.createProtocol("CreateRoom", RequestStatus.request.toString(), gson.toJson(gameRoom));
        client.respond(protocol);
    }


    @Override
    public void joinRoom(GameRoom gameRoom) {
        //TODO
    }

    @Override
    public void getGameRoomList() {
        Protocol protocol = protocolFactory.createProtocol("GetRooms", RequestStatus.request.toString(), null);
        client.respond(protocol);
    }

    public class ProxyCallback implements RoomListModule.Callback{
        private RoomListModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @BindCallback(event = "GetRooms", status = RequestStatus.success)
        @Override
        public void onGetRoomList(List<GameRoom> gameRooms) {
            callback.onGetRoomList(gameRooms);
        }

        @BindCallback(event = "CreateRoom", status = RequestStatus.success)
        @Override
        public void onNewRoom(GameRoom gameRoom) {
            callback.onNewRoom(gameRoom);
        }

        @BindCallback(event = "CloseRoom", status = RequestStatus.success)
        @Override
        public void onRoomClosed(GameRoom gameRoom) {
            callback.onRoomClosed(gameRoom);
        }

        @BindCallback(event = "UpdateRoom", status = RequestStatus.success)
        @Override
        public void onRoomUpdated(GameRoom gameRoom) {
            callback.onRoomUpdated(gameRoom);
        }


        @BindCallback(event = "JoinRooms", status = RequestStatus.success)
        @Override
        public void onJoinRoomSuccessfully(GameRoom gameRoom) {
            callback.onJoinRoomSuccessfully(gameRoom);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }

}
