package com.ood.clean.waterball.a1a2bsdk.core.base;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import gamecore.model.RequestStatus;


@Retention(RetentionPolicy.RUNTIME)
public @interface BindCallback {
    public String event();
    public RequestStatus status();


}
