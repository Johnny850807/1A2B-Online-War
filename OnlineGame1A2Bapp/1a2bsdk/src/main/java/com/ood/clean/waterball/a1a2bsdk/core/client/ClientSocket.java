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

import javax.inject.Inject;

import container.SocketIO;
import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;

import static com.ood.clean.waterball.a1a2bsdk.core.Secret.PORT;
import static com.ood.clean.waterball.a1a2bsdk.core.Secret.SERVER_ADDRESS;

public class ClientSocket implements Client{
    final static String TAG = "Socket";
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
        this.id = "ClientSocket";
        this.threadExecutor = threadExecutor;
        this.address = SERVER_ADDRESS;
        this.port = PORT;
    }

    @Override
    public void run() {
        try{
            Component.inject(this);
            SocketIO io = new SocketIO(new Socket(address, port));
            this.outputStream = new DataOutputStream(io.getOutputStream());
            this.inputStream = new DataInputStream(io.getInputStream());
            connected = true;
            listeningInputAsync();
            threadExecutor.postMain(NotifyCoreGameServerTask.CONNECTED);
        } catch (IOException e) {
            handleInputError(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listeningInputAsync(){
        new Thread(){
            @Override
            public void run() {
                try {
                    while(connected)
                        listenToNextInput();
                    Log.w(TAG, "Socket disconnected.");
                } catch (IOException err) {
                    handleInputError(err);
                } catch (RuntimeException err) {
                    handleInputError(err);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }.start();
    }


    private synchronized void listenToNextInput() throws IOException{
        if (connected)
        {
            String response = inputStream.readUTF();
            Log.d(TAG, "Received: " + response);
            threadExecutor.post(new Runnable() {
                @Override
                public void run() {
                    Protocol protocol = protocolFactory.createProtocol(response);
                    threadExecutor.postMain(new InvokeEventBusTask(protocol, eventBus));
                }
            });
        }
    }

    private void handleInputError(IOException err){
        Log.e(TAG, "Socket IOException.", err);
        threadExecutor.postMain(NotifyCoreGameServerTask.DISCONNECTED);
        threadExecutor.postMain(new PostErrorToEventBusTask(new ConnectionTimedOutException(err), eventBus));
    }

    private void handleInputError(RuntimeException err){
        Log.e(TAG, "Socket runtime error.", err);
        err.printStackTrace();
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
                    handleOutputError(protocol, err);
                }
            }
        });
    }

    private void handleOutputError(Protocol protocol, Exception err){
        Log.e(TAG, "Socket error while requesting.", err);
        threadExecutor.postMain(new PostErrorToEventBusTask(new ConnectionTimedOutException(err), eventBus));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public synchronized void disconnect() throws Exception {
        connected = false;
        threadExecutor.postMain(NotifyCoreGameServerTask.DISCONNECTED);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

}
