package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.EventBroadcastException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;

import static container.Constants.Events.InRoom.CHANGE_STATUS;
import static container.Constants.Events.InRoom.CLOSE_ROOM;
import static container.Constants.Events.InRoom.LAUNCH_GAME;
import static container.Constants.Events.InRoom.LEAVE_ROOM;
import static container.Constants.Events.RoomList.JOIN_ROOM;


public class InRoomModuleImp extends AbstractGameModule implements InRoomModule{
    protected RoomListModule roomListModule;
    protected UserSigningModule signingModule;
    protected ProxyCallback proxyCallback;

    public InRoomModuleImp(){
        this.signingModule = (UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING);
        this.roomListModule = (RoomListModule) CoreGameServer.getInstance().getModule(ModuleName.ROOMLIST);
    }

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
    public void changeStatus(ChangeStatusModel model) {
        Protocol protocol = protocolFactory.createProtocol(CHANGE_STATUS,
                RequestStatus.request.toString(), gson.toJson(model));
        client.broadcast(protocol);
    }

    @Override
    public void launchGame() {
        Protocol protocol = protocolFactory.createProtocol(CHANGE_STATUS,
                RequestStatus.request.toString(), gson.toJson(roomListModule.getCurrentGameRoom()));
        client.broadcast(protocol);
    }

    @Override
    public void bootPlayer(Player player) {
        // reuse the 'player left' event, to make that player leave from the room.
        Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM,
                RequestStatus.request.toString(), gson.toJson(new PlayerRoomIdModel(player.getId(),
                        roomListModule.getCurrentGameRoom().getId())));
        client.broadcast(protocol);
    }

    @Override
    public void closeRoom() {
        Protocol protocol = protocolFactory.createProtocol(CLOSE_ROOM,
                RequestStatus.request.toString(), gson.toJson(roomListModule.getCurrentGameRoom()));
        client.broadcast(protocol);
    }

    public class ProxyCallback implements InRoomModule.Callback{
        private InRoomModule.Callback callback;

        public ProxyCallback(InRoomModule.Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = JOIN_ROOM, status = RequestStatus.success)
        public void onPlayerJoined(PlayerRoomModel model) {
            if (!model.getGameRoom().equals(roomListModule.getCurrentGameRoom()))
                throw new IllegalStateException("The event you got was from the other room!");
            Log.d(TAG, "player " + model.getPlayer().getName() + " joined.");
            callback.onPlayerJoined(model);
        }

        @Override
        @BindCallback(event = CHANGE_STATUS, status = RequestStatus.success)
        public void onPlayerStatusChanged(ChangeStatusModel model) {
            if (!model.getRoomId().equals(roomListModule.getCurrentGameRoom().getId()))
                throw new IllegalStateException("The event you got was from the other room!");
            Log.d(TAG, "player's id " + model.getPlayerId() + "status changed, ready:" + model.isPrepare() +".");
            callback.onPlayerStatusChanged(model);
        }

        @Override
        @BindCallback(event = LEAVE_ROOM, status = RequestStatus.success)
        public void onPlayerLeft(PlayerRoomModel model) {
            if (!model.getGameRoom().equals(roomListModule.getCurrentGameRoom()))
                throw new IllegalStateException("The event you got was from the other room!");

            if(model.getPlayer().equals(signingModule.getCurrentPlayer()))
                this.onYouAreBooted();
            else
            {
                Log.d(TAG, "player " + model.getPlayer().getName() + " left.");
                callback.onPlayerLeft(model);
            }
        }

        @Override
        public void onYouAreBooted() {
            Log.d(TAG, "the current user got booted.");
            callback.onYouAreBooted();
        }

        @Override
        @BindCallback(event = CLOSE_ROOM, status = RequestStatus.success)
        public void onRoomClosed() {
            Log.d(TAG, "Room closed.");
            callback.onRoomClosed();
        }

        @Override
        @BindCallback(event = LAUNCH_GAME, status = RequestStatus.success)
        public void onGameLaunchedSuccessfully(GameRoom gameRoom) {
            if (!gameRoom.equals(roomListModule.getCurrentGameRoom()))
                throw new EventBroadcastException(LAUNCH_GAME);

            Log.d(TAG, "Game launched, info: " + gameRoom);
            callback.onGameLaunchedSuccessfully(gameRoom);
        }

        @Override
        @BindCallback(event = LAUNCH_GAME, status = RequestStatus.failed)
        public void onGameLaunchedFailed(GameRoom gameRoom) {
            if (!gameRoom.equals(roomListModule.getCurrentGameRoom()))
                throw new EventBroadcastException(LAUNCH_GAME);

            Log.d(TAG, "Game launched, info: " + gameRoom);
            callback.onGameLaunchedFailed(gameRoom);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
