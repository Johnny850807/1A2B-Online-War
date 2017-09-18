package com.ood.clean.waterball.a1a2bsdk.service.socket;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.ood.clean.waterball.a1a2bsdk.core.socketconnector.SocketConnector;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;


public class SocketListenerService extends IntentService {
    private EventBus eventBus;
    private SocketConnector socketConnector;

    public SocketListenerService(String name) {
        super(name);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        init();
    }

    private void init() {
        eventBus = EventBus.getInstance();
        socketConnector = SocketConnector.getInstance();
        socketConnector.start();
    }
}
