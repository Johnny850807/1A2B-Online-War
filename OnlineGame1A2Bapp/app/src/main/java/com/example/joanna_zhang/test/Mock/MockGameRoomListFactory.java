package com.example.joanna_zhang.test.Mock;


import com.example.joanna_zhang.test.Domain.Factory.GameRoomListFactory;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;

public class MockGameRoomListFactory implements GameRoomListFactory{

    @Override
    public List<GameRoom> createRoomList() {
        List<GameRoom> gameRooms = new ArrayList<>();
        gameRooms.add(new GameRoom(GameMode.DUEL1A2B, "對決", new Player("林宗億")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊", new Player("曾韋傑")));
        gameRooms.add(new GameRoom(GameMode.DUEL1A2B, "宗億小咖", new Player("我是大天才哈")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "一場10元",new Player("名子好難取喔")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊", new Player("= =")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "來玩啊啊啊啊", new Player("水球王")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "徵婆喔...",  new Player("張叔叔")));
        gameRooms.add(new GameRoom(GameMode.GROUP1A2B, "哈哈", new Player("豐緒  A帥")));
        return gameRooms;
    }

}
