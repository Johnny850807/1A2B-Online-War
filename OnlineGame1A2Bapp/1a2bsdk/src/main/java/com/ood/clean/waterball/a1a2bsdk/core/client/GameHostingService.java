package com.ood.clean.waterball.a1a2bsdk.core.client;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import javax.inject.Inject;

import container.base.Client;


/**
 * The main service hosting the socket connection to the server socket.
 */
public class GameHostingService extends IntentService {
    private @Inject Client client;
    private @Inject EventBus eventBus;

    public GameHostingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("socket", "Socket initializing starts...");
        Component.inject(this);
        init();
    }

    private void init() {
        new Thread(client).start();
        Log.d("socket", "Socket service has been started.");
    }
}
