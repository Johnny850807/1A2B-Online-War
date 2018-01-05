package com.example.joanna_zhang.test.mocks;

import container.core.Client;
import container.protocol.Protocol;
import gamecore.entity.Player;


public class MockClient implements Client {
    private Player player;

    public MockClient(Player player) {
        this.player = player;
    }

    @Override
    public void broadcast(Protocol protocol) {}

    @Override
    public String getId() {
        return player.getId();
    }

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    public void disconnect() throws Exception {

    }

    @Override
    public void run() {

    }
}
