package com.ood.clean.waterball.a1a2bsdk.core.mock;

import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.model.User;
import com.ood.clean.waterball.a1a2bsdk.core.signIn.UserSigningModule;
import com.ood.clean.waterball.a1a2bsdk.core.signIn.exceptions.UserNameFormatException;


public class MockUserSigningModule implements UserSigningModule {

    @Override
    public void signIn(@NonNull String name, @NonNull Callback callback) {
        if (name.length() < 2 || name.length() > 6)
            callback.onSignInFailed(new UserNameFormatException("name invalid"));
        else
            callback.onSignInSuccessfully(new User(name));
    }
}
