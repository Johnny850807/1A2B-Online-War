package com.ood.clean.waterball.a1a2bsdk;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.base.BindCallback;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class TestReflection implements UserSigningModule.Callback{
    private @Inject EventBus eventBus;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private Player player = new Player("Johnny");


    @Before
    public void setup(){
        Component.inject(this);

    }

    @Test
    public void testComponentReflection() throws Exception {
        assertNotNull(eventBus);
        assertNotNull(client);
        assertNotNull(protocolFactory);

        TestReflection test2 = new TestReflection();
        Component.inject(test2);

        assertEquals(eventBus, test2.eventBus);
        assertEquals(client, test2.client);
        assertEquals(protocolFactory, test2.protocolFactory);
    }

    @Test
    public void testBindingMethodOfEventBus() throws Exception{
        eventBus.registerCallback(this);
        String data = new Gson().toJson(player);
        eventBus.invoke(protocolFactory.createProtocol("SignIn", RequestStatus.success.toString(), data));
    }

    @Override
    public void onServerReconnected() {

    }

    @Override
    public void onError(@NonNull Throwable err) {
        fail();
    }

    @BindCallback(event = "SignIn", status = RequestStatus.success)
    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        assertEquals(this.player.getName(), player.getName());
    }


    @BindCallback(event = "SignIn", status = RequestStatus.failed)
    @Override
    public void onSignInFailed() {
        assertNull(player.getId());
    }

    @Override
    public void onLoadServerInformation(ServerInformation serverInformation) {

    }
}