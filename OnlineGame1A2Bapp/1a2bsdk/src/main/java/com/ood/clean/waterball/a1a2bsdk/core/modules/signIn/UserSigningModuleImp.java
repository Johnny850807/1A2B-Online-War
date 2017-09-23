package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.User;

public class UserSigningModuleImp implements UserSigningModule {
    private User currentUser;

    @Override
    public void signIn(@NonNull String name, @NonNull Callback callback) {
        try{

        }catch (Exception err){
            callback.onSignInFailed(err);
        }
    }

    @Override
    public User getCurrentUser() {
        if (currentUser == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentUser;
    }
}
