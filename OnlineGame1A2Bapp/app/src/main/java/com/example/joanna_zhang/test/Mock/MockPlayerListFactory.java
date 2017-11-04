package com.example.joanna_zhang.test.Mock;

import com.example.joanna_zhang.test.Domain.Factory.PlayerListFactory;
import com.ood.clean.waterball.a1a2bsdk.core.model.Player;

import java.util.ArrayList;
import java.util.List;

public class MockPlayerListFactory implements PlayerListFactory{
    @Override
    public List<Player> createPlayerList() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Lin"));
        players.add(new Player("Pan"));
        players.add(new Player("Joanna"));
        players.add(new Player("Lee"));
        players.add(new Player("Ana"));
        players.add(new Player("Gai"));
        return players;
    }
}
