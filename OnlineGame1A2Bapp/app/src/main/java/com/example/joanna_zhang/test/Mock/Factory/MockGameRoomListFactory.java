package com.example.joanna_zhang.test.Mock.Factory;


import com.example.joanna_zhang.test.Domain.Factory.GameRoomListFactory;
import com.example.joanna_zhang.test.Domain.GameMode;
import com.example.joanna_zhang.test.Domain.GameRoom;
import com.example.joanna_zhang.test.Mock.MockGameRoom;
import com.example.joanna_zhang.test.Mock.MockUser;

import java.util.ArrayList;
import java.util.List;

public class MockGameRoomListFactory implements GameRoomListFactory{

    @Override
    public List<GameRoom> createRoomList() {
        List<GameRoom> gameRooms = new ArrayList<>();
        gameRooms.add(new MockGameRoom("對決", GameMode.DUEL, new MockUser("林宗億")));
        gameRooms.add(new MockGameRoom("來玩啊啊啊啊", GameMode.FIGHT, new MockUser("曾韋傑")));
        gameRooms.add(new MockGameRoom("宗億小咖", GameMode.DUEL, new MockUser("我是大天才哈")));
        gameRooms.add(new MockGameRoom("一場10元", GameMode.FIGHT, new MockUser("名子好難取喔")));
        gameRooms.add(new MockGameRoom("來玩啊啊啊啊", GameMode.FIGHT, new MockUser("= =")));
        gameRooms.add(new MockGameRoom("來玩啊啊啊啊", GameMode.FIGHT, new MockUser("水球王")));
        gameRooms.add(new MockGameRoom("徵婆喔...", GameMode.FIGHT, new MockUser("張叔叔")));
        gameRooms.add(new MockGameRoom("哈哈", GameMode.FIGHT, new MockUser("豐緒  A帥")));
        return gameRooms;
    }

}
