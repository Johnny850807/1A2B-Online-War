package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.RoomExpiredCallback;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.PlayerRoomModel;

public interface InRoomModule extends GameModule{
    void registerCallback(Player currentPlayer, GameRoom currentRoom, InRoomModule.Callback callback);
    void unregisterCallBack(InRoomModule.Callback callback);

    /**
     * the player changes his status to the game.
     * @param model if the player wants to prepare.
     */
    public void changeStatus(ChangeStatusModel model);

    /**
     * the host launches the game.
     */
    public void launchGame();

    /**
     * boot the player.
     */
    public void bootPlayer(Player player);

    /**
     * close the room with evicting all the players out.
     */
    public void closeRoom();

    /**
     * leave from the current room
     */
    public void leaveRoom();

    public interface Callback extends GameCallBack, RoomExpiredCallback {

        /**
         * when any new player accesses the room.
         */
        public void onPlayerJoined(PlayerRoomModel model);

        /**
         * when any players in the room change his status such as to prepare.
         */
        public void onPlayerStatusChanged(ChangeStatusModel model);

        /**
         * when any player leaves the room.
         */
        public void onPlayerLeft(PlayerRoomModel model);

        /**
         * when the game launched successfully.
         * you will get the room which has a gameModel filled.
         */
        public void onGameLaunchedSuccessfully(GameRoom gameRoom);

        /**
         * when the game cannot be launched.
         */
        public void onGameLaunchedFailed(GameRoom gameRoom);

        /**
         * when the host kick you out of the room!
         */
        public void onYouAreBooted();

        /**
         * when the room got closed.
         */
        public void onRoomClosed();
    }
}
