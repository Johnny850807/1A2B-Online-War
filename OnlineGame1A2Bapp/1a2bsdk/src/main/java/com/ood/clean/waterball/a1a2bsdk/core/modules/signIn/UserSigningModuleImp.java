package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


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
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;


public class UserSigningModuleImp implements UserSigningModule {
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private Gson gson = new Gson();
    private Player currentPlayer;
    private ProxyCallback proxyCallback;

    public UserSigningModuleImp(){
        Component.inject(this);
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
    public void signIn(@NonNull String name) {
        Player player = new Player(name);
        Protocol protocol = protocolFactory.createProtocol("SignIn", RequestStatus.request.toString(), gson.toJson(player));
        client.respond(protocol);
    }


    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentPlayer;
    }

    @Override
    public void getServerInformation() {
        Protocol protocol = protocolFactory.createProtocol("GetServerInformation", RequestStatus.request.toString(), null);
        client.respond(protocol);
    }


    public class ProxyCallback implements UserSigningModule.Callback {
        private UserSigningModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = "SignIn", status = RequestStatus.success)
        public void onSignInSuccessfully(@NonNull Player player) {
            currentPlayer = player;
            callback.onSignInSuccessfully(player);
        }

        @Override
        @BindCallback(event = "SignIn", status = RequestStatus.failed)
        public void onSignInFailed() {
            callback.onSignInFailed();
        }

        @Override
        @BindCallback(event = "GetServerInformation", status = RequestStatus.success)
        public void onLoadServerInformation(ServerInformation serverInformation) {
            callback.onLoadServerInformation(serverInformation);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
