package com.ood.clean.waterball.a1a2bsdk.core.modules.games;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import java.util.List;

import gamecore.model.ContentModel;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;


public interface Duel1A2BModule extends GameModule {

    void registerCallback(Duel1A2BModule.Callback callback);
    void unregisterCallBack(Duel1A2BModule.Callback callback);

    void enterGame();

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
         * while you set the answer successfully.
         */
        void onSetAnswerSuccessfully(ContentModel setAnswerModel);

        void onSetAnswerUnsuccessfully(ContentModel setAnswerModel);

        /**
         * while you guessed successfully.
         * Note that the time you get the result from your guess is when both of the players commit the guess,
         * so when this method invoked simply means that your operation of guessing is successful. You should
         * wail for the onOneRoundOver() event for getting the result and showing on the view.
         */
        void onGuessSuccessfully(ContentModel guessModel);

        void onGuessUnsuccessfully(ContentModel guessModel);

        /**
         * when the both answers committed, the guessing phase started.
         * You should enable the guessing-related views.
         */
        void onGuessingStarted();

        /**
         * when both players committed the guess, this method will be invoked.
         * @param models the model of two players', contains the updated guess records.
         */
        void onOneRoundOver(List<Duel1A2BPlayerBarModel> models);

        /**
         * when the winner came out, the game will be over.
         * while this method invoked, you should show out the result on a dialog, closing the dialog and
         * finish the game being activity while the positive button of the dialog got clicked.
         * @param gameOverModel model contains the winner info.
         */
        void onGameOver(GameOverModel gameOverModel);
    }
}
