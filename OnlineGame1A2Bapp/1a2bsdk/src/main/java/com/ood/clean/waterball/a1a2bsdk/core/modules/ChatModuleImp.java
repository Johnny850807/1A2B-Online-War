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
import gamecore.model.ErrorMessage;
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
        public void onMessageSent(ChatMessage message) {
            Log.d(TAG, "message sent: " + message.getContent());
            callback.onMessageSent(message);
        }

        @Override
        @BindCallback(event = SEND_MSG, status = RequestStatus.failed)
        public void onMessageSendingFailed(ErrorMessage errorMessage) {
            Log.d(TAG, "message sending unsuccessfully: " + errorMessage);
            callback.onMessageSendingFailed(errorMessage);
        }

        @Override
        @BindCallback(event = SEND_MSG, status = RequestStatus.success)
        public void onMessageReceived(ChatMessage message) {
            Log.d(TAG, "message received: " + message.getContent());
            if(message.getPoster().equals(currentPlayer))
                this.onMessageSent(message);
            parseMessageContent(message);
            callback.onMessageReceived(message);
        }

        private void parseMessageContent(ChatMessage message) {
            try{
                char num = message.getContent().trim().charAt(0);  //for example: 1) ok
                switch (num)
                {
                    case '1':
                        onOkMessage(message);
                        break;
                    case '2':
                        onNoMessage(message);
                        break;
                    case '3':
                        onAwesomeMessage(message);
                        break;

                    case '4':
                        onQuicklyMessage(message);
                        break;
                    case '5':
                        onDamnMessage(message);
                        break;
                    case '6':
                        onGoodGameMessage(message);
                        break;
                    case '7':
                        onPleaseSetReadyMessage(message);
                        break;
                    case '8':
                        onPleaseStartMessage(message);
                        break;
                    case '9':
                        onSorryMessage(message);
                        break;
                }
            }catch (Exception err){
                Log.d(TAG, "the message '" + message.getContent() + "' is not the default chat util.");
            }
        }

        @Override
        public void onOkMessage(ChatMessage message) {
            callback.onOkMessage(message);
        }

        @Override
        public void onNoMessage(ChatMessage message) {
            callback.onNoMessage(message);
        }

        @Override
        public void onAwesomeMessage(ChatMessage message) {
            callback.onAwesomeMessage(message);
        }

        @Override
        public void onQuicklyMessage(ChatMessage message) {
            callback.onQuicklyMessage(message);
        }

        @Override
        public void onDamnMessage(ChatMessage message) {
            callback.onDamnMessage(message);
        }

        @Override
        public void onGoodGameMessage(ChatMessage message) {
            callback.onGoodGameMessage(message);
        }

        @Override
        public void onPleaseSetReadyMessage(ChatMessage message) {
            callback.onPleaseSetReadyMessage(message);
        }

        @Override
        public void onPleaseStartMessage(ChatMessage message) {
            callback.onPleaseStartMessage(message);
        }

        @Override
        public void onSorryMessage(ChatMessage message) {
            callback.onSorryMessage(message);
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
