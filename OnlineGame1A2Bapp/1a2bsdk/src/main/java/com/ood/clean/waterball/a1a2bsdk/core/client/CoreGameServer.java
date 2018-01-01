package com.ood.clean.waterball.a1a2bsdk.core.client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.GameModuleFactory;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.ReleaseGameModuleFactory;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.RequestStatus;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;
import static container.Constants.Events.RECONNECTED;

/**
    Facade pattern of the 1A2B sdk, contains all modules supporting your 1A2B app.
 **/
public final class CoreGameServer{
    private static final String TAG = "Socket";
    private static final GameModuleFactory moduleFactory = new ReleaseGameModuleFactory();
    private static CoreGameServer instance;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private @Inject
    EventBus eventBus;
    private Thread clientThread;

    private CoreGameServer(){
        Component.inject(this);
    }

    public static CoreGameServer getInstance(){
        if (instance == null)
            instance = new CoreGameServer();
        return instance;
    }

    /**
     * you should invoke this method at the first in your MainActivity,
     * the method starts the socket service to be running and therefore to ensure you to
     * listen to the events.
     *
     * @param context MainActivity
     */
    public void startEngine(@NonNull Context context){
        if (clientThread == null)
        {
            Log.d(TAG, "Socket initializing ..., Ip: " + SERVER_ADDRESS + ":" + PORT);
            clientThread = new Thread(client);
            clientThread.start();
        }
    }


    /**
     * @param name module's name enum
     * @return matching name module
     */
    public GameModule createModule(ModuleName name){
        return moduleFactory.createModule(name);
    }

    /**
     * close the connection to the server
     */
    public void shutdownConnection(){
        if (client != null)
            try {
                Log.d(TAG, "Shutting down the client connection.");
                client.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error occurs while Shutting down the client connection.");
                e.printStackTrace();
            }
    }

    // all onSocket callback method is only used for the client socket invoking.
    void onSocketConnected() {
        Protocol protocol = protocolFactory.createProtocol(RECONNECTED, RequestStatus.success.toString(), null);
        eventBus.invoke(protocol);
        Log.v(TAG, "invoke the Reconnected event to all callbacks in the eventbus.");
    }

    void onSocketDisconnected() {
        Log.v(TAG, "onSocketDisconnected");
    }

    void resendUnhandledEvents(){
        eventBus.resendNonHandledEvent();
    }

}
