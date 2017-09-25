package com.ood.clean.waterball.a1a2bsdk.core.commandparser.factory;


import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameCoreException;

public interface ExceptionFactory {
    GameCoreException create(int code, String message);
}
