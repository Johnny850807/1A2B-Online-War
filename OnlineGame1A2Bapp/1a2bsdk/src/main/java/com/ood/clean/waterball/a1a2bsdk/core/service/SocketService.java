package com.ood.clean.waterball.a1a2bsdk.core.service;


import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;

import java.io.IOException;
import java.net.Socket;

import communication.message.Message;
import gamecore.entity.Entity;
import userservice.ServiceIO;
import userservice.UserService;

import static com.ood.clean.waterball.a1a2bsdk.core.service.SocketService.Constant.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.service.SocketService.Constant.SERVER_ADDRESS;

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
        try {
            io = new SocketServiceIO(new Socket("35.194.206.10",5278));

        } catch (IOException e) {
            throw new ConnectionTimedOutException(e);
        }
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
        String SERVER_ADDRESS = "35.194.206.10";
        int PORT = 5278;
    }
}
