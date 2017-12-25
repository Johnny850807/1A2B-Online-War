package com.ood.clean.waterball.a1a2bsdk.core.modules.games;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import java.util.List;

import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;


public interface Due11A2BModule extends GameModule {
    /**
     * set the answer.
     */
    void setAnswer(String answer);

    /**
     * guess the opponent's answer.
     */
    void guess(String guess);

    public interface Callback extends GameCallBack{

        /**
         * when the both answers committed, the guessing phase started.
         * You should enable the guessing-related views.
         */
        void onGuessingStarted();

        /**
         *
         */
        void onOneRoundOver(List<Duel1A2BPlayerBarModel> models);

        /**
         *
         */
        void onGameOver(GameOverModel gameOverModel);
    }
}
