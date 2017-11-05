package com.ood.clean.waterball.a1a2bsdk.core.base;


import gamecore.model.RequestStatus;

public @interface Callback {
    public String event();
    public RequestStatus status();


}
