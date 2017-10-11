package com.ood.clean.waterball.a1a2bsdk.core.modules.mock;

import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.exceptions.UserNameFormatException;


public class MockUserSigningModule implements UserSigningModule {
    private User currentUser;

    @Override
    public void signIn(@NonNull String name, @NonNull Callback callback) {
        if (name.length() < 2 || name.length() > 6)
            callback.onError(new UserNameFormatException("name invalid"));
        else
            callback.onSignInSuccessfully(currentUser = new User(name));
    }

    @Override
    public User getCurrentUser() {
        if (currentUser == null)
            throw new IllegalStateException("There is no user signed in.");
        return currentUser;
    }
}
