package com.ood.clean.waterball.a1a2bsdk.core.service;


import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameIOException;

import java.io.IOException;
import java.net.Socket;

import communication.message.Message;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.entity.Entity;
import userservice.UserService;

import static com.ood.clean.waterball.a1a2bsdk.core.service.SocketService.Constant.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.service.SocketService.Constant.SERVER_ADDRESS;

public class SocketService implements UserService{
    private static SocketService instance = new SocketService();
    private ProtocolFactory protocolFactory;
    private SocketInputListener socketInputListener;
    private SocketServiceIO io;
    private String address;
    private int port;

    SocketService(){
        this.address = SERVER_ADDRESS;
        this.port = PORT;
    }

    public static void inject(ProtocolFactory protocolFactory){
        instance.protocolFactory = protocolFactory;
    }

    public static SocketService getInstance(){
        if (instance.protocolFactory == null)
            throw new IllegalStateException("The protocol factory of the socketservice should be injected before started.");
        return instance;
    }

    @Override
    public void run() {
        try {
            io = new SocketServiceIO(new Socket("35.194.206.10",5278));
            socketInputListener = new SocketInputListener(io, protocolFactory);
            new Thread(socketInputListener).start();
        } catch (IOException e) {
            throw new ConnectionTimedOutException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param message the message sended to the socket server
     */
    @Override
    public void respond(Message<? extends Entity> message) {
        try{
            String dataJson = new Gson().toJson(message.getData());
            Protocol protocol = protocolFactory.createProtocol(message.getEvent().toString(), message.getStatus().toString(),
                    dataJson);
            io.getOutputStream().writeUTF(protocol.toString());
        }catch (IOException err){
            throw new GameIOException(err);
        }catch (Exception err){
            err.printStackTrace();
        }
    }

    @Override
    public void disconnect() throws Exception {
        socketInputListener.close();
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
