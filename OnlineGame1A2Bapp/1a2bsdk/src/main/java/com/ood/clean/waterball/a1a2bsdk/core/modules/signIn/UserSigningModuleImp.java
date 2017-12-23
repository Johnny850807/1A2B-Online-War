package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.base.AbstractGameModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.CallbackException;

import container.protocol.Protocol;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;

import static container.Constants.Events.Signing.GETINFO;
import static container.Constants.Events.Signing.SIGNIN;
import static container.Constants.Events.Signing.SIGNOUT;


public class UserSigningModuleImp extends AbstractGameModule implements UserSigningModule{
    private Gson gson = new Gson();
    private Player currentPlayer;
    private ProxyCallback proxyCallback;

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
        Protocol protocol = protocolFactory.createProtocol(SIGNIN, RequestStatus.request.toString(), gson.toJson(player));
        client.respond(protocol);
    }

    @Override
    public void signOut() {
        if (currentPlayer == null)
            throw new IllegalStateException("No user signed in.");
        Protocol protocol = protocolFactory.createProtocol(SIGNOUT, RequestStatus.request.toString(), gson.toJson(currentPlayer));
        client.respond(protocol);
    }


    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentPlayer;
    }

    @Override
    public void getServerInformation() {
        Protocol protocol = protocolFactory.createProtocol(GETINFO, RequestStatus.request.toString(), null);
        client.respond(protocol);
    }


    public class ProxyCallback implements UserSigningModule.Callback {
        private UserSigningModule.Callback callback;

        public ProxyCallback(Callback callback) {
            this.callback = callback;
        }

        @Override
        @BindCallback(event = SIGNIN, status = RequestStatus.success)
        public void onSignInSuccessfully(@NonNull Player player) {
            currentPlayer = player;
            callback.onSignInSuccessfully(player);
        }

        @Override
        @BindCallback(event = SIGNIN, status = RequestStatus.failed)
        public void onSignInFailed() {
            callback.onSignInFailed();
        }

        @Override
        @BindCallback(event = GETINFO, status = RequestStatus.success)
        public void onLoadServerInformation(ServerInformation serverInformation) {
            callback.onLoadServerInformation(serverInformation);
        }

        @Override
        public void onError(@NonNull Throwable err) {
            callback.onError(err);
        }
    }
}
