package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.content.Context;
import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.base.ProxyModuleContext;

import gamecore.entity.Player;

public class UserSigningContext extends ProxyModuleContext implements UserSigningModule{
    private UserSigningModule userSigningModule;

    public UserSigningContext(Context context, UserSigningModule userSigningModule) {
        super(context);
        this.userSigningModule = userSigningModule;
    }

    @Override
    public void registerCallback(Callback callback) {
        userSigningModule.registerCallback(callback);
        bindingService();
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        userSigningModule.unregisterCallBack(callback);
        unbindService();
    }

    @Override
    public void getServerInformation() {
        userSigningModule.getServerInformation();
    }


    @Override
    public void signIn(@NonNull String name) {
        userSigningModule.signIn(name);
    }

    @Override
    public Player getCurrentPlayer() {
        return userSigningModule.getCurrentPlayer();
    }


}
