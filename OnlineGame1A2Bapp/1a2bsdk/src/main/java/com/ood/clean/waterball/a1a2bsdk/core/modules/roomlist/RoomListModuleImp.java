package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import java.util.List;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;

import static container.core.Constants.Events.InRoom.CLOSE_ROOM;
import static container.core.Constants.Events.InRoom.CLOSE_ROOM_TIME_EXPIRED;
import static container.core.Constants.Events.InRoom.LAUNCH_GAME;
import static container.core.Constants.Events.InRoom.LEAVE_ROOM;
import static container.core.Constants.Events.RECONNECTED;
import static container.core.Constants.Events.RoomList.CREATE_ROOM;
import static container.core.Constants.Events.RoomList.GET_ROOMS;
import static container.core.Constants.Events.RoomList.JOIN_ROOM;
import static container.core.Constants.Events.Signing.SIGNOUT_TIME_EXPIRED;


public class RoomListModuleImp extends AbstractGameModule implements RoomListModule {
    private ProxyCallback proxyCallback;
    private Player currentPlayer;

    @Override
    public void registerCallback(Player currentPlayer, Callback callback) {
        validate(currentPlayer);
        this.currentPlayer = currentPlayer;

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
        this.currentPlayer = null;
    }

    @Override
    public void createRoom(String roomName, GameMode gameMode) {
        GameRoom gameRoom = new GameRoom(gameMode, roomName, currentPlayer);
        Protocol protocol = protocolFactory.createProtocol(CREATE_ROOM, RequestStatus.request.toString(), gson.toJson(gameRoom));
        client.broadcast(protocol);
    }

    @Override
    public void joinRoom(GameRoom gameRoom) {
        Protocol protocol = protocolFactory.createProtocol(JOIN_ROOM, RequestStatus.request.toString(),
                gson.toJson(new PlayerRoomIdModel(currentPlayer.getId(), gameRoom.getId())));
        client.broadcast(protocol);
    }

    @Override
    public void getGameRoomList() {
        Protocol protocol = protocolFactory.createProtocol(GET_ROOMS, RequestStatus.request.toString(), null);
        client.broadcast(protocol);
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
            if(gameRoom.getHost().equals(currentPlayer))
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
        }

        @Override
        @BindCallback(event = CREATE_ROOM, status = RequestStatus.failed)
        public void onCreateRoomUnsuccessfully(ErrorMessage errorMessage) {
            Log.d(TAG, "Room created unsuccessfully: " + errorMessage);
            callback.onCreateRoomUnsuccessfully(errorMessage);
        }

        @Override
        @BindCallback(event = LAUNCH_GAME, status = RequestStatus.success)
        public void onRoomLaunched(GameRoom gameRoom) {
            Log.d(TAG,"Room launched: " + gameRoom);
            callback.onRoomLaunched(gameRoom);
        }

        @Override
        @BindCallback(event = CLOSE_ROOM, status = RequestStatus.success)
        public void onRoomClosed(GameRoom gameRoom) {
            Log.d(TAG, "Room closed: " + gameRoom);
            callback.onRoomClosed(gameRoom);
        }

        @Override
        @BindCallback(event = CLOSE_ROOM_TIME_EXPIRED, status = RequestStatus.success)
        public void onRoomClosedForExpired(GameRoom gameRoom) {
            Log.d(TAG, "Room expired: " + gameRoom);
            callback.onRoomClosedForExpired(gameRoom);
        }

        @Override
        @BindCallback(event = JOIN_ROOM, status = RequestStatus.success)
        public void onJoinRoomSuccessfully(PlayerRoomModel model) {
            if(model.getPlayer().equals(currentPlayer))
            {
                Log.d(TAG, "Join Room successfully: " + model.getGameRoom());
                callback.onJoinRoomSuccessfully(model);
            }
            else
                this.onPlayerJoined(model);
        }

        @Override
        @BindCallback(event = JOIN_ROOM, status = RequestStatus.failed)
        public void onJoinRoomUnsuccessfully(ErrorMessage errorMessage) {
            callback.onJoinRoomUnsuccessfully(errorMessage);
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
        @BindCallback(event = SIGNOUT_TIME_EXPIRED, status = RequestStatus.success)
        public void onPlayerLeisureTimeExpired() {
            Log.d(TAG, "The player leisure time is expired.");
            callback.onPlayerLeisureTimeExpired();
        }

        @Override
        public void onRoomUpdated(GameRoom gameRoom) {
            Log.d(TAG, "Room info updated: "  + gameRoom);
            callback.onRoomUpdated(gameRoom);
        }

        @Override
        @BindCallback(event = RECONNECTED, status = RequestStatus.success)
        public void onServerReconnected() {
            callback.onServerReconnected();
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }

}
