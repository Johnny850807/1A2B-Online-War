package com.ood.clean.waterball.a1a2bsdk.core.client;

import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import container.protocol.Protocol;


class InvokeEventBusTask implements Runnable{
    private Protocol protocol;
    private EventBus eventBus;
    InvokeEventBusTask(Protocol protocol, EventBus eventBus) {
        this.protocol = protocol;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try{
            eventBus.invoke(protocol);
        }catch (Exception err){
            Log.e(ClientSocket.TAG, "Error occurs while invoking event bus.", err);
        }
    }
}
