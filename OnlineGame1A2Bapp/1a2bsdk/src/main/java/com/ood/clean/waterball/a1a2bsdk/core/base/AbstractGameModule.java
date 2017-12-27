package com.ood.clean.waterball.a1a2bsdk.core.base;


import android.util.Log;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;
import utils.MyGson;

public abstract class AbstractGameModule implements GameModule{
    protected static final String TAG = "GameModule";
    protected @Inject EventBus eventBus;
    protected @Inject Client client;
    protected @Inject ProtocolFactory protocolFactory;
    protected Gson gson = MyGson.getGson();

    public AbstractGameModule(){
        Log.d(TAG, "initing " + getClass().getSimpleName() + " ...");
        Component.inject(this);
        Log.d(TAG, "injection completed.");
    }

}
