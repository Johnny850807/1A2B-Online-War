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
import container.protocol.Protocol;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;


/**
 * The main service hosting the socket connection to the server socket.
 */
public class GameHostingService extends Service {
    private static final String TAG = "GameHostingService";
    private @Inject Client client;
    private @Inject EventBus eventBus;
    private String address = SERVER_ADDRESS;
    private int port = PORT;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Socket initializing ..., Ip: " + SERVER_ADDRESS + ":" + PORT);
        Component.inject(this);
        new Thread(client).start();
        return START_STICKY;
    }

    public void send(Protocol protocol){
        Log.d(TAG, "Sending protocol : " + protocol);
        client.respond(protocol);
        Log.d(TAG, "Sending completed.");
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
