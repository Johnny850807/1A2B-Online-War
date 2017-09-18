package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions;


import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameCoreException;

public class UserNameFormatException extends GameCoreException{

    public UserNameFormatException(String message) {
        super(message);
    }
}
