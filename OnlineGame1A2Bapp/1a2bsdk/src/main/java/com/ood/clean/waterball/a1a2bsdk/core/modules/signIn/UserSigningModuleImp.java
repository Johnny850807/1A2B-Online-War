package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;


public class UserSigningModuleImp implements UserSigningModule {
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private Player currentPlayer;

    public UserSigningModuleImp(){
        Component.inject(this);
    }

    @Override
    public void signIn(@NonNull String name, @NonNull UserSigningModule.Callback callback) {
        Player player = new Player(name);
        String json = new Gson().toJson(player);
        Protocol protocol = protocolFactory.createProtocol("SignIn", RequestStatus.request.toString(), json);
        eventBus.registerCallback(callback);
        client.respond(protocol);
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentPlayer;
    }
}
