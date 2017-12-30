package com.ood.clean.waterball.a1a2bsdk.core.modules.signIn;


import android.support.annotation.NonNull;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import gamecore.entity.Player;
import gamecore.model.ServerInformation;

public interface UserSigningModule extends GameModule{
    String TAG = "Signing";
    void registerCallback(UserSigningModule.Callback callback);
    void unregisterCallBack(UserSigningModule.Callback callback);

    /**
     * @param name - user's name to be signed
     */
    void signIn(@NonNull String name);

    /**
     * the current user signs out.
     */
    void signOut(Player currentPlayer);

    void getServerInformation();

    /**
     * used to listen to the sign event
     */
    public interface Callback extends GameCallBack{

        /**
         * @param player the player returned includes the sign in info
         */
        void onSignInSuccessfully(@NonNull Player player);

        /**
         * invoked while the user signed failed.
         */
        void onSignInFailed();


        void onLoadServerInformation(ServerInformation serverInformation);
    }
}
