package com.example.joanna_zhang.test;

import android.app.Application;

import com.example.joanna_zhang.test.Utils.DefaultTtsVoiceManager;
import com.example.joanna_zhang.test.Utils.SoundManager;
import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;


public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DefaultTtsVoiceManager.init(getApplicationContext(), new SoundManager(getApplicationContext()));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CoreGameServer.getInstance().shutdownConnection();
        DefaultTtsVoiceManager.getInstance().release();
    }
}
