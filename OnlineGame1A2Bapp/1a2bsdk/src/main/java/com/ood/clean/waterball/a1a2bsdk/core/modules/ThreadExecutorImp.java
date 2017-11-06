package com.ood.clean.waterball.a1a2bsdk.core.modules;


import com.ood.clean.waterball.a1a2bsdk.core.ThreadExecutor;

public class ThreadExecutorImp implements ThreadExecutor{

    @Override
    public void post(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public void postDelayed(Runnable runnable, long delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    new Thread(runnable).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
