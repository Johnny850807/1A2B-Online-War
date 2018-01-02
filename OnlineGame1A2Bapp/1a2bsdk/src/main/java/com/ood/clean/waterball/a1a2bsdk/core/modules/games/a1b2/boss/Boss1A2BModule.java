package com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss;


import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

public interface Boss1A2BModule extends OnlineGameModule{
    public void attack();


    public interface Callback extends OnlineGameModule.Callback{
        public void onNextPlayerTurn();
        public void onAttackingSuccessfully();
        public void onGameOver();
    }
}
