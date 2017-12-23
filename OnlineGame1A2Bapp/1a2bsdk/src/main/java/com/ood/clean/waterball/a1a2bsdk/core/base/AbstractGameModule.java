package com.ood.clean.waterball.a1a2bsdk.core.base;


import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;

import javax.inject.Inject;

import container.base.Client;
import container.protocol.ProtocolFactory;

public abstract class AbstractGameModule implements GameModule{
    protected static @Inject EventBus eventBus;
    protected static @Inject Client client;
    protected static @Inject ProtocolFactory protocolFactory;

    public AbstractGameModule(){
        Component.inject(this);
    }

}
