package com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist;

import com.ood.clean.waterball.a1a2bsdk.core.client.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;


public class MockRoomListModule implements RoomListModule {
    private Callback callback;
    private List<GameRoom> gameRooms = new ArrayList<>();

    public MockRoomListModule() {
        gameRooms.add(new GameRoom(GameMode.DUEL1A2B, "對決", new Player("林宗億")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊", new Player("曾韋傑")));
        gameRooms.add(new GameRoom(GameMode.DUEL1A2B, "宗億小咖", new Player("我是大天才哈")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "一場10元", new Player("名子好難取喔")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊",new Player("= =")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊", new Player("水球王")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "徵婆喔...", new Player("張叔叔")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "哈哈", new Player("豐緒  A帥")));
    }

    @Override
    public void registerCallback(Player currentPlayer, Callback callback) {
        this.callback = callback;
    }

    @Override
    public void unregisterCallBack(Callback callback) {
        callback = null;
    }

    @Override
    public void createRoom(String roomName, GameMode gameMode) {
        UserSigningModule userSigningModule = (UserSigningModule) CoreGameServer.getInstance().createModule(ModuleName.SIGNING);
        //callback.onCreateRoomSuccessfully(new GameRoom(gameMode, roomName, userSigningModule.getCurrentPlayer()));
    }

    @Override
    public void joinRoom(GameRoom gameRoom) {
    }

    @Override
    public void getGameRoomList() {
        callback.onGetRoomList(gameRooms);
    }


}
