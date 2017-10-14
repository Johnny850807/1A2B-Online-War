package com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.factory;

import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameCoreException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;


public class ReleaseExceptionFactory implements ExceptionFactory {

    @Override
    public GameCoreException create(int code, String message) {
        switch (code)
        {
            case 100:
                return new UserNameFormatException(message);
        }
        throw new RuntimeException("Didn't find exception code : " + code + ", message : " + message);
    }
}
