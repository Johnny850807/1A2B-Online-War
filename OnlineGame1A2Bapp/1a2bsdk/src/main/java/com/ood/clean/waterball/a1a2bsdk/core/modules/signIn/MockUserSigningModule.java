package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;

import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;

import gamecore.entity.Player;
import gamecore.model.ServerInformation;


public class MockUserSigningModule implements UserSigningModule {
    private Callback callback;
    private Player currentPlayer;

    @Override
    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        callback = null;
    }

    @Override
    public void signIn(@NonNull String name) {
        if (name.length() < 2 || name.length() > 6)
            callback.onError(new UserNameFormatException("name invalid"));
        else
            callback.onSignInSuccessfully(currentPlayer = new Player(name));
    }

    @Override
    public void signOut() {
        currentPlayer = null;
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == null)
            throw new IllegalStateException("There is no user signed in.");
        return currentPlayer;
    }

    @Override
    public void getServerInformation() {
        callback.onLoadServerInformation(new ServerInformation(5, 15));
    }
}
