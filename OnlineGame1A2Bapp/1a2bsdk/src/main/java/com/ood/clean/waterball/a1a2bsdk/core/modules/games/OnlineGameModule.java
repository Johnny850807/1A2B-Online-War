package com.ood.clean.waterball.a1a2bsdk.core.modules.games;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.RoomExpiredCallback;

import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomModel;
import gamecore.model.games.a1b2.duel.core.GameOverModel;

public interface OnlineGameModule extends GameModule{

    /**
     * this method is important for all the games that you should invoke in onResume() just 'after'
     * registering the callback to the eventbus.
     *
     * This method is a mechanism to make sure all the players have entered the game view and registered the callback
     * , therefore the game started event will be emitted while all players are ready.
     */
    void enterGame();

    /**
     * leave from the current game and the current room.
     */
    void leaveGame();

    public interface Callback extends GameCallBack, RoomExpiredCallback {
        /**
         * when everybody enters the game, the game started.
         */
        void onGameStarted();

        /**
         * when the winner came out, the game will be over.
         * @param gameOverModel model contains the winner info.
         */
        void onGameOver(GameOverModel gameOverModel);

        /**
         * when the opponent left from the game.
         */
        void onPlayerLeft(PlayerRoomModel model);

        /**
         * when the gameroom closed bt the host
         */
        void onGameClosed(GameRoom gameRoom);
    }
}
