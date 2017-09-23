package com.ood.clean.waterball.a1a2bsdk.core.base.exceptions;


public class GameCoreException extends RuntimeException{
    public GameCoreException(String message) {
        super(message);
    }

    public GameCoreException(Throwable cause) {
        super(cause);
    }
}
