package com.ood.clean.waterball.a1a2bsdk.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.factory.moduleinflater.MockGameModuleInflater;
import com.ood.clean.waterball.a1a2bsdk.core.model.GameServerInformation;
import com.ood.clean.waterball.a1a2bsdk.service.GameService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
    Facade pattern of the 1A2B sdk, contains all modules supporting your 1A2B app.
 **/
public final class CoreGameServer {
    private static CoreGameServer instance;

    private Map<ModuleName,GameModule> moduleMap;

    private CoreGameServer(){
        moduleMap = Collections.checkedMap(new HashMap<ModuleName, GameModule>(),ModuleName.class, GameModule.class);

        //TODO Mocked
        new MockGameModuleInflater().onPrepareModules(moduleMap);
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
        //todo run all modules and start the service of socket
        context.startService(new Intent(context, GameService.class));
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


    public void getInformation(@NonNull CoreGameServer.Callback callback){
        callback.onGetInformation(new GameServerInformation(5,2));
    }

    public interface Callback{
        void onGetInformation(GameServerInformation gameServerInformation);
    }


}
