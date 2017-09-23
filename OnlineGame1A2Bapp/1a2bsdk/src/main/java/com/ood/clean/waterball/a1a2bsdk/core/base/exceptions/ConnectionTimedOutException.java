package com.ood.clean.waterball.a1a2bsdk.core.base.exceptions;


public class ConnectionTimedOutException extends GameCoreException {

    public ConnectionTimedOutException(String message) {
        super(message);
    }

    public ConnectionTimedOutException(Throwable cause) {
        super(cause);
    }

}
