package com.ood.clean.waterball.a1a2bsdk.core.service;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import userservice.ServiceIO;

public class SocketServiceIO implements ServiceIO{
    private Socket socket;

    public SocketServiceIO(Socket socket) {
        this.socket = socket;
    }

    @Override
    public DataInputStream getInputStream() throws Exception {
        return new DataInputStream(socket.getInputStream());
    }

    @Override
    public DataOutputStream getOutputStream() throws Exception {
        return new DataOutputStream(socket.getOutputStream());
    }
}
