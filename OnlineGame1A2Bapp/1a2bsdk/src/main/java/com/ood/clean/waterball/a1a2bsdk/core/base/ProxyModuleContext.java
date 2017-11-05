package com.ood.clean.waterball.a1a2bsdk.core.base;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ood.clean.waterball.a1a2bsdk.core.client.GameHostingService;

/**
 * The proxy holds a context instance from a certain Activity for binding the GameHostingService.
 */
public class ProxyModuleContext {
    private Context context;
    private GameHostingService gameHostingService;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            GameHostingService.GameServiceBinder mybinder = (GameHostingService.GameServiceBinder) binder;
            gameHostingService = mybinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            gameHostingService = null;
        }
    };

    public ProxyModuleContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public GameHostingService getGameHostingService() {
        return gameHostingService;
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    public void bindingService(){
        Intent intent = new Intent(context, GameHostingService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(){
        context.unbindService(connection);
    }
}
