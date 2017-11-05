package com.ood.clean.waterball.a1a2bsdk.core.client;


import android.util.Log;

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
    private final static String TAG = "socket";
    private @Inject ProtocolFactory protocolFactory;
    private @Inject EventBus eventBus;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String address;
    private int port;

    public ClientSocket(){
        this.address = SERVER_ADDRESS;
        this.port = PORT;
        Component.inject(this);
    }

    @Override
    public void run() {
        try {
            Log.d(TAG, "Socket initializing ..., Ip: " + address + ":" + port);
            SocketIO io = new SocketIO(new Socket(address, port));
            this.outputStream = new DataOutputStream(io.getOutputStream());
            this.inputStream = new DataInputStream(io.getInputStream());
            Log.d(TAG, "Socket initializing completed.");

            listeningInput();
        } catch (IOException e) {
            Log.d(TAG, "IOException occurs while reading to the socket.");
            throw new ConnectionTimedOutException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listeningInput() throws IOException {
        while(true)
        {
            String response = inputStream.readUTF();
            Log.d(TAG, "Response : " + response);
            Protocol protocol = protocolFactory.createProtocol(response);
            Log.d(TAG, "Protocol created from the response: " + protocol);
            eventBus.invoke(protocol);
        }
    }


    @Override
    public void respond(Protocol protocol) {
        try{
            Log.d(TAG, "Sending protocol : " + protocol);
            outputStream.writeUTF(protocol.toString());
            Log.d(TAG, "Sending completed.");
        }catch (IOException err){
            Log.d(TAG, "IOException occurs while writing to the socket.");
            eventBus.error(new ConnectionTimedOutException(err));
        }catch (Exception err){
            err.printStackTrace();
            eventBus.error(new GameCoreException(new ConnectionTimedOutException(err)));
        }
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
