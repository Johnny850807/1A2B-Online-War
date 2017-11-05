package com.ood.clean.waterball.a1a2bsdk.core.client;


import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.GameCoreException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.inject.Inject;

import container.SocketIO;
import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;

public class ClientSocket implements Client{
    private @Inject ProtocolFactory protocolFactory;
    private @Inject EventBus eventBus;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String address;
    private int port;

    public ClientSocket(){
        this.address = SERVER_ADDRESS;
        this.port = PORT;
    }

    @Override
    public void run() {
        try {
            Component.inject(this);
            SocketIO io = new SocketIO(new Socket(address, port));
            this.outputStream = new DataOutputStream(io.getOutputStream());
            this.inputStream = new DataInputStream(io.getInputStream());

            listeningInput();
        } catch (IOException err) {
            err.printStackTrace();
            eventBus.error(new ConnectionTimedOutException(err));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listeningInput() throws IOException {
        while(true)
        {
            String response = inputStream.readUTF();

            Protocol protocol = protocolFactory.createProtocol(response);
            eventBus.invoke(protocol);
        }
    }


    @Override
    public void respond(Protocol protocol) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    outputStream.writeUTF(protocol.toString());
                }catch (IOException err){
                    err.printStackTrace();
                    eventBus.error(new ConnectionTimedOutException(err));
                }catch (Exception err){
                    err.printStackTrace();
                    eventBus.error(new GameCoreException(new ConnectionTimedOutException(err)));
                }
            }
        }).start();
    }

    @Override
    public void disconnect() throws Exception {
        // TODO
    }


    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

}
