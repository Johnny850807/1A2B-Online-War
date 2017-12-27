package com.ood.clean.waterball.a1a2bsdk.core.client;


import android.util.Log;

import com.ood.clean.waterball.a1a2bsdk.core.Component;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.ThreadExecutor;
import com.ood.clean.waterball.a1a2bsdk.core.base.exceptions.ConnectionTimedOutException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import javax.inject.Inject;

import container.SocketIO;
import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.RequestStatus;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;

public class ClientSocket implements Client{
    private final static String TAG = "Socket";
    private @Inject ProtocolFactory protocolFactory;
    private @Inject EventBus eventBus;
    private String id;
    private ThreadExecutor threadExecutor;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String address;
    private int port;
    private boolean connected = false;

    public ClientSocket(ThreadExecutor threadExecutor){
        this.id = UUID.randomUUID().toString();
        this.threadExecutor = threadExecutor;
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
            connected = true;
            listeningInput();
        } catch (Exception err) {
            Log.e(TAG, "Socket error while initializing.", err);
            threadExecutor.postMain(new PostErrorToEventBusTask(new ConnectionTimedOutException(err)));
        }
    }

    private void listeningInput() throws IOException {
        while(connected)
        {
            String response = inputStream.readUTF();
            Log.d(TAG, "Received: " + response);
            threadExecutor.post(new Runnable() {
                @Override
                public void run() {
                    Protocol protocol = protocolFactory.createProtocol(response);
                    threadExecutor.postMain(new InvokeEventBusTask(protocol));
                }
            });
        }
        Log.w(TAG, "Socket disconnected.");
    }

    @Override
    public void broadcast(Protocol protocol) {
        threadExecutor.post(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.i(TAG, "Request: "+ protocol);
                    outputStream.writeUTF(protocol.toString());
                }catch (Exception err){
                    Log.e(TAG, "Socket error while requesting.", err);
                    threadExecutor.postMain(new InvokeEventBusTask(protocolFactory.createProtocol(protocol.getEvent(),
                            RequestStatus.failed.toString(), protocol.getData())));
                    threadExecutor.postMain(new PostErrorToEventBusTask(new ConnectionTimedOutException(err)));
                }
            }
        });
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void disconnect() throws Exception {
        connected = false;
    }

    private class InvokeEventBusTask implements Runnable{
        Protocol protocol;
        private InvokeEventBusTask(Protocol protocol) {
            this.protocol = protocol;
        }
        @Override
        public void run() {
            try{
                eventBus.invoke(protocol);
            }catch (Exception err){
                Log.e(TAG, "Error occurs while invoking event bus.", err);
            }
        }
    }

    private class PostErrorToEventBusTask implements Runnable{
        private Exception err;
        private PostErrorToEventBusTask(Exception err) {
            this.err = err;
        }
        @Override
        public void run() {
            try{
                eventBus.error(err);
            }catch (Exception err){
                Log.e(TAG, "Error occurs while posting error to event bus.", err);
            }
        }
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

}
