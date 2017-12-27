package com.ood.clean.waterball.a1a2bsdk.core.client;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import javax.inject.Inject;

import container.base.Client;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;


/**
 * The main service hosting the socket connection to the server socket.
 */
public class GameHostingService extends Service {
    private static final String TAG = "Service";
    private @Inject Client client;
    private @Inject EventBus eventBus;
    private String address = SERVER_ADDRESS;
    private int port = PORT;
    private Thread clientThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Component.inject(this);
        Log.d(TAG, "Socket starting.");
        if (clientThread == null || !clientThread.isAlive())
        {
            Log.d(TAG, "Socket initializing ..., Ip: " + SERVER_ADDRESS + ":" + PORT);
            clientThread = new Thread(client);
            clientThread.start();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new GameServiceBinder();
    }

    public class GameServiceBinder extends Binder{
        public GameHostingService getService(){
            return GameHostingService.this;
        }
    }
}
