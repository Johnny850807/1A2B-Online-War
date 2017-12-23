package com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom;


import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.exceptions.HostCannotPrepareException;
import com.ood.clean.waterball.a1a2bsdk.core.modules.inRoom.exceptions.PlayerNotPreparedException;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.PlayerStatus;

public interface InRoomModule extends GameModule{
    void registerCallback(InRoomModule.Callback callback);
    void unregisterCallBack(InRoomModule.Callback callback);

    /**
     * the player changes his status to the game.
     * @param prepare if the player wants to prepare.
     * @exception HostCannotPrepareException if you are a host, you shouldn't be able to change the status.
     */
    public void changeStatus(boolean prepare);

    /**
     * the host launches the game.
     * @exception PlayerNotPreparedException the players should prepare before you launch the game.
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

    public interface Callback extends GameCallBack{

        /**
         * when any new player accesses the room.
         */
        public void onNewPlayer(PlayerStatus playerStatus);

        /**
         * when any players in the room change his status such as to prepare.
         */
        public void onPlayerStatusChanged(PlayerStatus playerStatus);

        /**
         * when any player leaves the room.
         */
        public void onPlayerLeft(PlayerStatus playerStatus);

        /**
         * when the game launched successfully.
         * you will get the room which has a gameModel filled.
         */
        public void onGameLaunchedSuccessfully(GameRoom gameRoom);

        /**
         * when the game cannot be launched.
         */
        public void onGameLaunchedFailed();
    }
}
