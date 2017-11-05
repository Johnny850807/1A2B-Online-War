package com.ood.clean.waterball.a1a2bsdk;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModuleImp;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;

public class TestUserSigningAPI implements UserSigningModule.Callback{
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private Player player = new Player("Johnny");
    private UserSigningModule userSigningModule;

    @Before
    public void setup(){
        Component.inject(this);
        userSigningModule = new UserSigningModuleImp();
        userSigningModule.registerCallback(this);
    }

    @Test
    public void testSignInAPI() throws InterruptedException {
        new Thread(client).start();
        Thread.sleep(2000); // waiting for the socket setup.
        client.respond(protocolFactory.createProtocol("SignIn", RequestStatus.request.toString(), new Gson().toJson(player)));
        synchronized (this)
        {
            wait();
            Assert.assertNotNull(player.getName());

        }
    }

    @After
    public void tearDown(){
        try {
            userSigningModule.unregisterCallBack(this);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        this.player = player;
        notify();
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onLoadServerInformation(ServerInformation serverInformation) {

    }
}
