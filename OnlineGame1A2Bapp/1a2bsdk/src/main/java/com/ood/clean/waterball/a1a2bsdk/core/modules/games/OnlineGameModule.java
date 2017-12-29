package com.ood.clean.waterball.a1a2bsdk.core.modules.games;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

public interface OnlineGameModule extends GameModule{

    /**
     * this method is important for all the games that you should invoke in onResume() just 'after'
     * registering the callback to the eventbus.
     *
     * This method is a mechanism to make sure all the players have entered the game view and registered the callback
     * , therefore the game started event will be emitted while all players are ready.
     */
    void enterGame();

    public interface Callback extends GameCallBack{
        void onGameStarted();
    }
}
