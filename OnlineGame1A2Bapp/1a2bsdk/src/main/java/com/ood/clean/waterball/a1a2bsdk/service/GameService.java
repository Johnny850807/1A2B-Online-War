package com.ood.clean.waterball.a1a2bsdk.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.service.SocketService;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import userservice.UserService;


public class GameService extends IntentService {
    private EventBus eventBus;
    private UserService socketService = SocketService.getInstance();

    public GameService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("socket", "Socket initializing starts...");
        init();
    }

    private void init() {
        eventBus = EventBus.getInstance();
        new Thread(socketService).start();
        Log.d("socket", "Socket service has been started.");
    }
}
