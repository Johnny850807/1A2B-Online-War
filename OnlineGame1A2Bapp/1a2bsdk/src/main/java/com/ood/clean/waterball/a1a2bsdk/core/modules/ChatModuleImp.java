package com.ood.clean.waterball.a1a2bsdk.core.modules;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import container.protocol.Protocol;
import gamecore.entity.ChatMessage;
import gamecore.model.RequestStatus;

import static container.Constants.Events.Chat.SEND_MSG;

public class ChatModuleImp extends AbstractGameModule implements ChatModule {
    protected UserSigningModule signingModule;
    protected RoomListModule roomListModule;
    protected ProxyCallback proxyCallback;

    public ChatModuleImp() {
        signingModule = (UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING);
        roomListModule = (RoomListModule) CoreGameServer.getInstance().getModule(ModuleName.ROOMLIST);
    }

    @Override
    public void registerCallback(Callback callback) {
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
            if(message.getPoster().equals(signingModule.getCurrentPlayer()))
                this.onMessageSent(message);
            else
                callback.onMessageReceived(message);
        }

        @Override
        public void onMessageSent(ChatMessage message) {
            callback.onMessageSent(message);
        }

        @Override
        @BindCallback(event = SEND_MSG, status = RequestStatus.failed)
        public void onMessageSendingFailed(ChatMessage message) {
            callback.onMessageSendingFailed(message);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
