package com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel;

import com.ood.clean.waterball.a1a2bsdk.core.base.ChatRoomModule;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.model.ChatMessage;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.game1a2b.duel.model.Game1A2BDuelStatus;
import com.ood.clean.waterball.a1a2bsdk.core.modules.game.model.GuessRecord;


public interface Duel1A2BGameModule extends ChatRoomModule, GameModule{
    void registerCallback(Callback callback);
    void unregisterCallBack(Callback callback);
    void startGame();
    void sendChatMessage(ChatMessage message);
    void setPlayerAnswer(Player player, String answer);
    void loadGameStatus();
    void guess(String guess);

    public interface Callback extends ChatRoomModule.Callback, GameCallBack{
        /**
         * @param player the player whose answer has been set successfully.
         */
        void onPlayerAnswerSetCompleted(Player player);

        /**
         * the method will be invoked when the game begin.
         */
        void onGameStarted();

        /**
         * the method will be invoked when the duel game started, both players can start guessing each other's answer.
         */
        void onDuelStarting();

        /**
         * @param player the player who guessed.
         * @param guessRecord the guess with the result appended to the record.
         */
        void onNewGuessAppended(Player player, GuessRecord guessRecord);

        /**
         * @param winner the winner who wins the game with the correct result 4A.
         * @param opponentAnswer the answer of the opponent's, you should simply expose all answers.
         */
        void onPlayerWin(Player winner, String opponentAnswer);

        /**
         * The method will be invoked after you invoke {@code Duel1A2BGameModule.loadGameStatus()}.
         * @param game1A2BDuelStatus lasted status of 1A2B Game.
         */
        void onGameStatusLoaded(Game1A2BDuelStatus game1A2BDuelStatus);
    }
}
