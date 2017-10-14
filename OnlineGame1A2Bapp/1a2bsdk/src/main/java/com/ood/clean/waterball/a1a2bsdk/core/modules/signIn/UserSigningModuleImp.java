package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.Player;

public class UserSigningModuleImp implements UserSigningModule {
    private Player currentPlayer;

    @Override
    public void signIn(@NonNull String name, @NonNull Callback callback) {
        try{

        }catch (Exception err){
            callback.onSignInFailed(err);
        }
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no signed in user.");
        return currentPlayer;
    }
}
