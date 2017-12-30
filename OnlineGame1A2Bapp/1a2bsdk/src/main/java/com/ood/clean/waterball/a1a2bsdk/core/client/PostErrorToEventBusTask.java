package com.ood.clean.waterball.a1a2bsdk.core.client;

import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.EventBus;


class PostErrorToEventBusTask implements Runnable{
    private Exception err;
    private EventBus eventBus;

    PostErrorToEventBusTask(Exception err, EventBus eventBus) {
        this.err = err;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try{
            eventBus.error(err);
        }catch (Exception err){
            Log.e(ClientSocket.TAG, "Error occurs while posting error to event bus.", err);
        }
    }
}
