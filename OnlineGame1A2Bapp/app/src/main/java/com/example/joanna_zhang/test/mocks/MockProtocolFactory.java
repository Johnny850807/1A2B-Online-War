package com.example.joanna_zhang.test.mocks;

import container.protocol.Protocol;
import container.protocol.ProtocolFactory;



public class MockProtocolFactory implements ProtocolFactory {
    @Override
    public Protocol createProtocol(String s) {
        throw new RuntimeException("Not supported.");
    }

    @Override
    public Protocol createProtocol(String s, String s1, String s2) {
        throw new RuntimeException("Not supported.");
    }
}
