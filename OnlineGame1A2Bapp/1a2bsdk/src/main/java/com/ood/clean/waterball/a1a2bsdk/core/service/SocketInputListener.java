package com.ood.clean.waterball.a1a2bsdk.core.service;


import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameIOException;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.CommandParser;
import com.ood.clean.waterball.a1a2bsdk.core.commandparser.factory.CommandParserFactory;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;

import command.base.Command;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;

public class SocketInputListener implements Runnable, Closeable{
    private DataInputStream dataInputStream;
    private ProtocolFactory protocolFactory;
    private boolean running = true;

    public SocketInputListener(SocketServiceIO socketServiceIO, ProtocolFactory protocolFactory) throws Exception {
        this.dataInputStream = socketServiceIO.getInputStream();
        this.protocolFactory = protocolFactory;
    }

    @Override
    public void run() {
        while(running)
        {
            try {
                String input = dataInputStream.readUTF();
                Protocol protocol = protocolFactory.createProtocol(input);
                CommandParser parserLinkedList = CommandParserFactory.getInstance().createCommandParserLinkedList();
                Command command = parserLinkedList.parse(protocol);
                command.execute();
            } catch (IOException e) {
                throw new GameIOException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        dataInputStream.close();
    }
}
