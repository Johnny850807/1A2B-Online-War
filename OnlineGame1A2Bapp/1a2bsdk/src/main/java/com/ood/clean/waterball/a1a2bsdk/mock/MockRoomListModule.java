package com.ood.clean.waterball.a1a2bsdk.mock;

import android.support.annotation.Nullable;

import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameRoom;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameMode;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;


public class MockRoomListModule implements RoomListModule {
    private Callback callback;
    private List<GameRoom> gameRooms = new ArrayList<>();

    public MockRoomListModule() {
        gameRooms.add(new GameRoom("對決", GameMode.DUEL1A2B, new Player("林宗億")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("曾韋傑")));
        gameRooms.add(new GameRoom("宗億小咖", GameMode.DUEL1A2B, new Player("我是大天才哈")));
        gameRooms.add(new GameRoom("一場10元", GameMode.GROUP1A2B, new Player("名子好難取喔")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("= =")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("水球王")));
        gameRooms.add(new GameRoom("徵婆喔...", GameMode.GROUP1A2B, new Player("張叔叔")));
        gameRooms.add(new GameRoom("哈哈", GameMode.GROUP1A2B, new Player("豐緒  A帥")));
    }

    @Override
    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        callback = null;
    }

    @Override
    public void createRoom(String roomName, GameMode gameMode) {
        UserSigningModule userSigningModule = (UserSigningModule) CoreGameServer.getInstance().getModule(ModuleName.SIGNING);
        callback.onCreatedRoomSuccessfully(new GameRoom(roomName, gameMode, userSigningModule.getCurrentPlayer()));
    }

    @Override
    public void searchRoom(String keyName, @Nullable GameMode gameMode) {
        List<GameRoom> results = new ArrayList<>();
        for (GameRoom room : gameRooms)
        {
            if (gameMode != null)
                if (room.getGameMode() != gameMode)
                    continue;
            if (room.getName().contains(keyName) || room.getRoomHost().getName().contains(keyName))
                results.add(room);
        }
        callback.onGetRoomList(results);
    }

    @Override
    public void joinRoom(GameRoom gameRoom) {
        callback.onJoinRoomSuccessfully(gameRoom);
    }

    @Override
    public void getGameRoomList() {
        callback.onGetRoomList(gameRooms);
    }


}
