package com.ood.clean.waterball.a1a2bsdk.core;



public interface ThreadExecutor {
    void post(Runnable runnable);
    void postDelayed(Runnable runnable, long delay);
}
