package com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss;


import android.content.Context;

import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.games.a1b2.GameOverModel;
import gamecore.model.games.a1b2.boss.AttackActionModel;
import gamecore.model.games.a1b2.boss.NextTurnModel;

public interface Boss1A2BModule extends OnlineGameModule{
    void registerCallback(Context context, Player currentPlayer, GameRoom currentGameRoom, Boss1A2BModule.Callback callback);
    void unregisterCallBack(Boss1A2BModule.Callback callback);
    public void setAnswer(String answer);
    public void attack(String guess);

    public interface Callback extends OnlineGameModule.Callback{
        public void onSetAnswerSuccessfully(ContentModel contentModel);
        public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage);
        public void onAttackSuccessfully(ContentModel contentModel);
        public void onAttackUnsuccessfully(ErrorMessage errorMessage);
        public void onNexAttackAction(AttackActionModel attackActionModel);
        public void onYourTurn(NextTurnModel nextTurnModel);
        public void onGameOver(GameOverModel gameOverModel);
    }

}
