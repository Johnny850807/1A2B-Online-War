package com.ood.clean.waterball.a1a2bsdk.core.modules.games.a1b2.duel;

import android.content.Context;

import com.ood.clean.waterball.a1a2bsdk.core.modules.games.OnlineGameModule;

import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ContentModel;
import gamecore.model.ErrorMessage;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;


public interface Duel1A2BModule extends OnlineGameModule{

    void registerCallback(Context context,  Player currentPlayer, GameRoom currentGameRoom, Duel1A2BModule.Callback callback);
    void unregisterCallBack(Duel1A2BModule.Callback callback);


    /**
     * set the answer.
     */
    void setAnswer(String answer);

    /**
     * guess the opponent's answer.
     */
    void guess(String guess);


    public interface Callback extends OnlineGameModule.Callback {

        /**
         * while you set the answer successfully.
         */
        void onSetAnswerSuccessfully(ContentModel setAnswerModel);

        void onSetAnswerUnsuccessfully(ErrorMessage errorMessage);

        /**
         * while you guessed successfully.
         * Note that the time you get the result from your guess is when both of the players commit the guess,
         * so when this method invoked simply means that your operation of guessing is successful. You should
         * wail for the onOneRoundOver() event for getting the result and showing on the view.
         */
        void onGuessSuccessfully(ContentModel guessModel);

        void onGuessUnsuccessfully(ErrorMessage errorMessage);

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


    }
}
