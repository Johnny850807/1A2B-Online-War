package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.model.User;

public interface UserSigningModule extends GameModule{
    String TAG = "Signing module";

    /**
     * @param name - user's name to be signed
     * @param callback - the call back interface listening to the feedback of the signing event
     */
    void signIn(@NonNull String name, @NonNull UserSigningModule.Callback callback);

    /**
     * @return the user who has signed in to the roomlist
     */
    User getCurrentUser();

    /**
     * used to listen to the sign event
     */
    public interface Callback extends GameCallBack{

        /**
         * @param user the user returned includes the sign in info
         */
        void onSignInSuccessfully(@NonNull User user);
    }
}
