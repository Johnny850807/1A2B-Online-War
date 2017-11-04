package com.example.joanna_zhang.test.Mock;


import com.example.joanna_zhang.test.Domain.Factory.GameRoomListFactory;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameRoom;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.model.GameMode;

import java.util.ArrayList;
import java.util.List;

public class MockGameRoomListFactory implements GameRoomListFactory{

    @Override
    public List<GameRoom> createRoomList() {
        List<GameRoom> gameRooms = new ArrayList<>();
        gameRooms.add(new GameRoom("對決", GameMode.DUEL1A2B, new Player("林宗億")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("曾韋傑")));
        gameRooms.add(new GameRoom("宗億小咖", GameMode.DUEL1A2B, new Player("我是大天才哈")));
        gameRooms.add(new GameRoom("一場10元", GameMode.GROUP1A2B, new Player("名子好難取喔")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("= =")));
        gameRooms.add(new GameRoom("來玩啊啊啊啊", GameMode.GROUP1A2B, new Player("水球王")));
        gameRooms.add(new GameRoom("徵婆喔...", GameMode.GROUP1A2B, new Player("張叔叔")));
        gameRooms.add(new GameRoom("哈哈", GameMode.GROUP1A2B, new Player("豐緒  A帥")));
        return gameRooms;
    }

}
