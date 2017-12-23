package com.ood.clean.waterball.a1a2bsdk.core.modules;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.ChatMessage;
import gamecore.model.RequestStatus;

public class ChatModuleImp implements ChatModule {
    protected @Inject EventBus eventBus;
    protected @Inject Client client;
    protected @Inject ProtocolFactory protocolFactory;
    protected Gson gson = new Gson();
    protected ProxyCallback proxyCallback;

    public ChatModuleImp() {
        Component.inject(this);
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
        String json = gson.toJson(message);
        Protocol protocol = protocolFactory.createProtocol("SendChatMessage", RequestStatus.request.toString(), json);
        client.respond(protocol);
    }

    public class ProxyCallback implements ChatModule.Callback{
        private ChatModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = "SendChatMessage", status = RequestStatus.success)
        public void onMessageReceived(ChatMessage message) {
            callback.onMessageReceived(message);
        }

        @Override
        @BindCallback(event = "SendChatMessage", status = RequestStatus.success)
        public void onMessageSent(ChatMessage message) {
            callback.onMessageSent(message);
        }

        @Override
        @BindCallback(event = "SendChatMessage", status = RequestStatus.success)
        public void onMessageSendingFailed(ChatMessage message) {
            callback.onMessageSendingFailed(message);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
