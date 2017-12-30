package com.ood.clean.waterball.a1a2bsdk.core.base;


import android.support.annotation.NonNull;

/**
 * mark interface
 */
public interface GameCallBack {
    void onServerReconnected();
    void onError(@NonNull Throwable err);
}
