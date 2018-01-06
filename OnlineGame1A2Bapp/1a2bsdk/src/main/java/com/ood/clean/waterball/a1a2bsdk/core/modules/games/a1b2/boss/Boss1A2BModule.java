package com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.boss;


import android.content.Context;

import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;

public interface Boss1A2BModule extends OnlineGameModule{
    void registerCallback(Context context, Player currentPlayer, GameRoom currentGameRoom, Boss1A2BModule.Callback callback);
    void unregisterCallBack(Boss1A2BModule.Callback callback);
    public void setAnswer(String answer);
    public void attack(String guess);

    public interface Callback extends OnlineGameModule.Callback{

        /**
         * when everybody enters the game, the game started.
         */
        void onGameStarted(SpiritsModel spiritsModel);

        /**
         * when everybody has set the answer, the attacking phase started.
         * The next turn event will be emitted following this event to indicate which is the first player to attack.
         */
        void onAttackingPhaseStarted();

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
        public void onNextTurn(NextTurnModel nextTurnModel);

        /**
         * when then player sets the answer successfully
         */
        public void onGameOver(GameOverModel gameOverModel);
    }

}
