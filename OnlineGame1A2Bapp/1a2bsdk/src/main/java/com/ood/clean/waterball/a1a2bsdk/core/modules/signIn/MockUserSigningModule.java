package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;

import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

import gamecore.entity.Player;


public class MockUserSigningModule implements UserSigningModule {
    private Player currentPlayer;

    @Override
    public void signIn(@NonNull String name, @NonNull Callback callback) {
        if (name.length() < 2 || name.length() > 6)
            callback.onError(new UserNameFormatException("name invalid"));
        else
            callback.onSignInSuccessfully(currentPlayer = new Player(name));
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no user signed in.");
        return currentPlayer;
    }
}
