package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.List;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;

import static container.Constants.Events.InRoom.CLOSE_ROOM;
import static container.Constants.Events.InRoom.LEAVE_ROOM;
import static container.Constants.Events.RoomList.CREATE_ROOM;
import static container.Constants.Events.RoomList.GET_ROOMS;
import static container.Constants.Events.RoomList.JOIN_ROOM;


public class RoomListModuleImp extends AbstractGameModule implements RoomListModule {
    private GameRoom currentGameRoom;
    private UserSigningModule signingModule;
    private ProxyCallback proxyCallback;

    public RoomListModuleImp() {
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
        Protocol protocol = protocolFactory.createProtocol(CREATE_ROOM, RequestStatus.request.toString(), gson.toJson(gameRoom));
        client.broadcast(protocol);
    }


    @Override
    public void joinRoom(GameRoom gameRoom) {
        Protocol protocol = protocolFactory.createProtocol(JOIN_ROOM, RequestStatus.request.toString(),
                gson.toJson(new PlayerRoomIdModel(signingModule.getCurrentPlayer().getId(), gameRoom.getId())));
        client.broadcast(protocol);
    }

    @Override
    public void getGameRoomList() {
        Protocol protocol = protocolFactory.createProtocol(GET_ROOMS, RequestStatus.request.toString(), null);
        client.broadcast(protocol);
    }

    @Override
    public GameRoom getCurrentGameRoom() {
        return currentGameRoom;
    }

    @Override
    public void cleanCurrentGameRoom(){
        this.currentGameRoom = null;
    }

    public class ProxyCallback implements RoomListModule.Callback{
        private RoomListModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = GET_ROOMS, status = RequestStatus.success)
        public void onGetRoomList(List<GameRoom> gameRooms) {
            Log.d(TAG, "gameRooms got, size:" + gameRooms.size());
            callback.onGetRoomList(gameRooms);
        }

        @Override
        @BindCallback(event = CREATE_ROOM, status = RequestStatus.success)
        public void onNewRoom(GameRoom gameRoom) {
            if(gameRoom.getHost().equals(signingModule.getCurrentPlayer()))
                this.onCreateRoomSuccessfully(gameRoom);
            else
            {
                Log.d(TAG, "New Room created: " + gameRoom);
                callback.onNewRoom(gameRoom);
            }
        }

        @Override
        public void onCreateRoomSuccessfully(GameRoom gameRoom) {
            Log.d(TAG, "Room created successfully: " + gameRoom);
            callback.onCreateRoomSuccessfully(gameRoom);
            currentGameRoom = gameRoom;
        }

        @Override
        @BindCallback(event = CREATE_ROOM, status = RequestStatus.failed)
        public void onCreateRoomUnsuccessfully(GameRoom gameRoom) {
            Log.d(TAG, "Room created unsuccessfully: " + gameRoom);
            callback.onCreateRoomUnsuccessfully(gameRoom);
        }

        @BindCallback(event = CLOSE_ROOM, status = RequestStatus.success)
        @Override
        public void onRoomClosed(GameRoom gameRoom) {
            Log.d(TAG, "Room closed: " + gameRoom);
            callback.onRoomClosed(gameRoom);
        }

        @Override
        @BindCallback(event = JOIN_ROOM, status = RequestStatus.success)
        public void onJoinRoomSuccessfully(PlayerRoomModel model) {
            if(model.getPlayer().equals(signingModule.getCurrentPlayer()))
            {
                Log.d(TAG, "Join Room successfully: " + model.getGameRoom());
                callback.onJoinRoomSuccessfully(model);
                currentGameRoom = model.getGameRoom();
            }
            else
                this.onPlayerJoined(model);
        }

        @Override
        public void onPlayerJoined(PlayerRoomModel model) {
            Log.d(TAG, "Player " + model.getPlayer().getName() + " joined to the room " + model.getGameRoom());
            callback.onPlayerJoined(model);
        }

        @Override
        @BindCallback(event = LEAVE_ROOM, status = RequestStatus.success)
        public void onPlayerLeft(PlayerRoomModel model) {
            Log.d(TAG, "Player " + model.getPlayer().getName() + " left from the room " + model.getGameRoom());
            callback.onPlayerLeft(model);
        }

        @Override
        public void onRoomUpdated(GameRoom gameRoom) {
            Log.d(TAG, "Room info updated: "  + gameRoom);
            callback.onRoomUpdated(gameRoom);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }

}
