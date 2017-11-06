package com.ood.clean.waterball.a1a2bsdk.core;


import android.os.Handler;

public class HandlerExecutor implements ThreadExecutor {
    private Handler handler = new Handler();

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void postDelayed(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }
}
