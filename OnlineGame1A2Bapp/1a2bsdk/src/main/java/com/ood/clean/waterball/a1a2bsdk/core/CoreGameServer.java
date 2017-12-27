package com.ood.clean.waterball.a1a2bsdk.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;

/**
    Facade pattern of the 1A2B sdk, contains all modules supporting your 1A2B app.
 **/
public final class CoreGameServer {
    private static final String TAG = "Socket";
    private static CoreGameServer instance;
    private @Inject Client client;
    private @Inject ProtocolFactory protocolFactory;
    private @Inject EventBus eventBus;
    private Thread clientThread;
    private String address = SERVER_ADDRESS;
    private int port = PORT;

    private Map<ModuleName,GameModule> moduleMap;

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
        if (moduleMap == null)
            prepareModules();

        if (clientThread == null || !clientThread.isAlive())
        {
            Log.d(TAG, "Socket initializing ..., Ip: " + SERVER_ADDRESS + ":" + PORT);
            clientThread = new Thread(client);
            clientThread.start();
        }
    }

    public void prepareModules(){
        moduleMap = Collections.checkedMap(new HashMap<ModuleName, GameModule>(),ModuleName.class, GameModule.class);
        new ReleaseGameModuleInflater().onPrepareModules(moduleMap);
    }

    /**
     * @param name module's name enum
     * @return matching name module
     */
    public GameModule getModule(ModuleName name){

        if (!moduleMap.containsKey(name))
            throw new IllegalArgumentException(name.toString() + " module is not prepared.");

        return moduleMap.get(name);
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
}
