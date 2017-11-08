package com.ood.clean.waterball.a1a2bsdk.core;



public interface ThreadExecutor {
    /**
     * post the task needed to be executing in the background.
     */
    void post(Runnable runnable);

    /**
     * post the task needed to be executing on the main thread, expecting handling the UI updating in this method.
     */
    void postMain(Runnable runnable);
}
