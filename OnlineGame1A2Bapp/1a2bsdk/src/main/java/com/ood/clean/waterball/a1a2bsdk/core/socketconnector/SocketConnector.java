package com.ood.clean.waterball.a1a2bsdk.core.socketconnector;


import static com.ood.clean.waterball.a1a2bsdk.core.socketconnector.SocketConnector.Constant.*;

public class SocketConnector{
    private static SocketConnector instance;
    private String address;
    private int port;

    SocketConnector(){
        this.address = SERVER_ADDRESS;
        this.port = PORT;
    }

    public static SocketConnector getInstance(){
        if (instance == null)
            instance = new SocketConnector();
        return instance;
    }

    public void start(){

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
