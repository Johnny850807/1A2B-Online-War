package com.ood.clean.waterball.a1a2bsdk.core;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;

import container.protocol.Protocol;

public interface EventBus {
    void registerCallback(GameCallBack gameCallBack);
    void unregisterCallback(GameCallBack gameCallBack);
    void error(Exception err);
    void invoke(Protocol protocol);
}
