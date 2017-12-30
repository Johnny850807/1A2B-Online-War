package com.ood.clean.waterball.a1a2bsdk.core.client;



enum NotifyCoreGameServerTask implements Runnable {
    CONNECTED, DISCONNECTED;

    @Override
    public void run() {
        CoreGameServer coreGameServer = CoreGameServer.getInstance();
        switch (this)
        {
            case CONNECTED:
                coreGameServer.onSocketConnected();
                break;
            case DISCONNECTED:
                coreGameServer.onSocketDisconnected();
                break;
        }
    }
}
