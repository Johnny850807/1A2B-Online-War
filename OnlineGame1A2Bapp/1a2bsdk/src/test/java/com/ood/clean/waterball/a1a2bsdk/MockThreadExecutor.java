package com.ood.clean.waterball.a1a2bsdk;


import com.ood.clean.waterball.a1a2bsdk.core.ThreadExecutor;

public class MockThreadExecutor implements ThreadExecutor{

    @Override
    public void post(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void postMain(Runnable runnable) {
        new Thread(runnable).start();
    }

}
