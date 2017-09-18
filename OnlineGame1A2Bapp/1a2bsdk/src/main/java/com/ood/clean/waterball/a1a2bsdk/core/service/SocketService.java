package com.ood.clean.waterball.a1a2bsdk.core.service;


import communication.message.Message;
import gamecore.entity.Entity;
import userservice.ServiceIO;
import userservice.UserService;

import static com.ood.clean.waterball.a1a2bsdk.core.service.SocketService.Constant.*;

public class SocketService implements UserService{
    private static SocketService instance;
    private ServiceIO io;
    private String address;
    private int port;

    SocketService(){
        this.address = SERVER_ADDRESS;
        this.port = PORT;
    }

    public static SocketService getInstance(){
        if (instance == null)
            instance = new SocketService();
        return instance;
    }

    @Override
    public void run() {
        //TODO start socket listening
    }


    /**
     * @param message the message sended to the socket server
     */
    @Override
    public void respond(Message<? extends Entity> message) {

    }

    @Override
    public void disconnect() throws Exception {

    }


    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public interface Constant {
        String SERVER_ADDRESS = "52...."; // todo
        int PORT = 5278;
    }
}
