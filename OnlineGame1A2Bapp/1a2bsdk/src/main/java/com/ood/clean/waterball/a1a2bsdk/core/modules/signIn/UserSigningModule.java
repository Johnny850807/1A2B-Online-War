package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;

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
    Player getCurrentPlayer();

    /**
     * used to listen to the sign event
     */
    public interface Callback extends GameCallBack{

        /**
         * @param player the player returned includes the sign in info
         */
        void onSignInSuccessfully(@NonNull Player player);


        /**
         * @param err signed failed
         */
        void onSignInFailed(@NonNull Exception err);
    }
}
