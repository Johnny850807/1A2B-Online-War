package com.ood.clean.waterball.a1a2bsdk.core.service;


import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameIOException;
import com.ood.clean.waterball.a1a2bsdk.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.IOException;

import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;

public class SocketInputListener implements Runnable{
    private SocketServiceIO socketServiceIO;
    private DataInputStream dataInputStream;
    private ProtocolFactory protocolFactory;
    private EventBus eventBus = EventBus.getInstance();

    public SocketInputListener(SocketServiceIO socketServiceIO, ProtocolFactory protocolFactory) throws Exception {
        this.socketServiceIO = socketServiceIO;
        this.dataInputStream = socketServiceIO.getInputStream();
        this.protocolFactory = protocolFactory;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String input = dataInputStream.readUTF();
                Protocol protocol = protocolFactory.createProtocol(input);
            } catch (IOException e) {
                throw new GameIOException(e);
            }
        }
    }
}
