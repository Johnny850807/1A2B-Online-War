package com.ood.clean.waterball.a1a2bsdk.core.modules;


import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import container.protocol.Protocol;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;

import static container.Constants.Events.Chat.SEND_MSG;
import static container.Constants.Events.RECONNECTED;

public class ChatModuleImp extends AbstractGameModule implements ChatModule {
    protected ProxyCallback proxyCallback;
    protected Player currentPlayer;
    protected GameRoom currentGameRoom;

    @Override
    public void registerCallback(Player currentPlayer, GameRoom currentRoom, Callback callback) {
        validate(currentPlayer);
        validate(currentRoom);
        this.currentPlayer = currentPlayer;
        this.currentGameRoom = currentRoom;

        if (this.proxyCallback != null)
            callback.onError(new CallbackException());
        this.proxyCallback = new ChatModuleImp.ProxyCallback(callback);
        eventBus.registerCallback(proxyCallback);
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        if (this.proxyCallback == null || this.proxyCallback.callback != callback)
            callback.onError(new CallbackException());
        eventBus.unregisterCallback(proxyCallback);
        this.proxyCallback = null;
        this.currentPlayer = null;
        this.currentGameRoom = null;
    }

    @Override
    public void sendMessage(ChatMessage message) {
        Protocol protocol = protocolFactory.createProtocol(SEND_MSG, RequestStatus.request.toString(), gson.toJson(message));
        client.broadcast(protocol);
    }

    public class ProxyCallback implements ChatModule.Callback{
        private ChatModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = SEND_MSG, status = RequestStatus.success)
        public void onMessageReceived(ChatMessage message) {
            Log.d(TAG, "message received: " + message.getContent());
            if(message.getPoster().equals(currentPlayer))
                this.onMessageSent(message);
            callback.onMessageReceived(message);
        }

        @Override
        public void onMessageSent(ChatMessage message) {
            Log.d(TAG, "message sent: " + message.getContent());
            callback.onMessageSent(message);
        }

        @Override
        @BindCallback(event = SEND_MSG, status = RequestStatus.failed)
        public void onMessageSendingFailed(ChatMessage message) {
            Log.d(TAG, "message sending unsuccessfully: " + message.getContent());
            callback.onMessageSendingFailed(message);
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
