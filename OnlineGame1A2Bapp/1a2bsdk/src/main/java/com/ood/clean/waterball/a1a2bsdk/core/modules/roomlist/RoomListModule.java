package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist;

import com.ood.clean.waterball.a1a2bsdk.core.base.GameCallBack;
import com.ood.clean.waterball.a1a2bsdk.core.base.GameModule;

import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ErrorMessage;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomModel;


public interface RoomListModule extends GameModule{
    void registerCallback(Player currentPlayer, RoomListModule.Callback callback);
    void unregisterCallBack(RoomListModule.Callback callback);
    void createRoom(String roomName, GameMode gameMode);
    void joinRoom(GameRoom gameRoom);
    void getGameRoomList();

    public interface Callback extends GameCallBack {
        /**
         * This method will be called when you want to get the room list or search by a keyword, the result roomlist
         * will be passed as a parameter to the method.
         */
        void onGetRoomList(List<GameRoom> gameRooms);

        /**
         * This method will be called if any of the room created by the online player.
         */
        void onNewRoom(GameRoom gameRoom);

        /**
         * This method will be called if the room created successfully by self.
         */
        void onCreateRoomSuccessfully(GameRoom gameRoom);

        /**
         * This method will be called if the room created unsuccessfully by self.
         */
        void onCreateRoomUnsuccessfully(ErrorMessage errorMessage);

        /**
         * This method will be called if any of the room launched by the online player.
         */
        void onRoomLaunched(GameRoom gameRoom);

        /**
         * This method will be called if any of the room closed by the online player.
         */
        void onRoomClosed(GameRoom gameRoom);

        /**
         * This method will be called if any of the room's info modified by the host.
         */
        void onRoomUpdated(GameRoom gameRoom);

        /**
         * This method will be called when you join to the room successfully.
         */
        void onJoinRoomSuccessfully(PlayerRoomModel model);

        void onJoinRoomUnsuccessfully(ErrorMessage errorMessage);

        /**
         * This method will be called when someone joined to any room.
         * You should update the room player amount on the UI.
         */
        void onPlayerJoined(PlayerRoomModel model);

        /**
         * This method will be called when someone left from any room.
         * You should update the room player amount on the UI.
         */
        void onPlayerLeft(PlayerRoomModel model);

        /**
         * when the player stay in no-action  in the room list for a given long time,
         * that means the player is kicked out of the online.
         */
        void onPlayerLeisureTimeExpired();
    }
}
