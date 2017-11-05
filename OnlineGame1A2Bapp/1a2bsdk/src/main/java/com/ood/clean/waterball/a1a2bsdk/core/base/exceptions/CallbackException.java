package com.ood.clean.waterball.a1a2bsdk.core.base.exceptions;


public class CallbackException extends GameCoreException{

    public CallbackException() {
        super("The callback is not registered/unregistered at the right lifecycle.");
    }

}
