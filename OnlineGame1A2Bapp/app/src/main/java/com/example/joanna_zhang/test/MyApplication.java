package com.example.joanna_zhang.test;

import android.app.Application;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;


public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CoreGameServer.getInstance().startEngine(getApplicationContext());
    }
}
