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
        /**
         * when then player sets the answer successfully
         */
        public void onSetAnswerSuccessfully(ContentModel contentModel);

        /**
         * when then player sets the answer unsuccessfully
         */
        public void onSetAnswerUnsuccessfully(ErrorMessage errorMessage);

        /**
         * when then player attacks the boss and produce the attack results successfully
         */
        public void onAttackSuccessfully(ContentModel contentModel);

        /**
         * when then player sets the answer unsuccessfully
         */
        public void onAttackUnsuccessfully(ErrorMessage errorMessage);

        /**
         * when any spirit finishes his attack action.
         */
        public void onNextAttackAction(AttackActionModel attackActionModel);

        /**
         * when then player sets the answer successfully
         */
        public void onYourTurn(NextTurnModel nextTurnModel);

        /**
         * when then player sets the answer successfully
         */
        public void onGameOver(GameOverModel gameOverModel);
    }

}
