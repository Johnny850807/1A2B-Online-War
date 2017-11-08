package com.ood.clean.waterball.a1a2bsdk;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ood.clean.waterball.a1a2bsdk.core.CoreGameServer;
import com.ood.clean.waterball.a1a2bsdk.core.EventBus;
import com.ood.clean.waterball.a1a2bsdk.core.ModuleName;
import com.ood.clean.waterball.a1a2bsdk.core.ReflectionEventBus;
import com.ood.clean.waterball.a1a2bsdk.core.client.ClientSocket;
import com.ood.clean.waterball.a1a2bsdk.core.modules.ThreadExecutorImp;
import com.ood.clean.waterball.a1a2bsdk.core.modules.roomlist.RoomListModule;
import com.ood.clean.waterball.a1a2bsdk.core.modules.signIn.UserSigningModule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import container.base.Client;
import container.protocol.ProtocolFactory;
import container.protocol.XOXOXDelimiterFactory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;

public class TestUserSigningAPI implements UserSigningModule.Callback, RoomListModule.Callback {
    private EventBus eventBus;
    private Client client;
    private ProtocolFactory protocolFactory;
    private Gson gson = new Gson();
    private UserSigningModule userSigningModule;
    private RoomListModule roomListModule;
    private Player player;
    private List<GameRoom> gameRooms;
    private GameRoom gameRoom;
    private ServerInformation serverInformation;

    @Before
    public void setup(){
        CoreGameServer coreGameServer = CoreGameServer.getInstance();
        eventBus = new ReflectionEventBus();
        client = new ClientSocket(new ThreadExecutorImp());
        protocolFactory = new XOXOXDelimiterFactory();
        coreGameServer.prepareModules(); // make sure the modules prepared.
        userSigningModule = (UserSigningModule) coreGameServer.getModule(ModuleName.SIGNING);
        roomListModule = (RoomListModule) coreGameServer.getModule(ModuleName.ROOMLIST);
        roomListModule.registerCallback(this);
        userSigningModule.registerCallback(this);
    }

    @Test
    public void testSignInAPI() throws InterruptedException {
        new Thread(client).start();
        Thread.sleep(2000); // waiting for the socket setup.
        client.respond(protocolFactory.createProtocol("SignIn", RequestStatus.request.toString(), new Gson().toJson(new Player("Johnny"))));
        client.respond(protocolFactory.createProtocol("GetServerInformation", RequestStatus.request.toString(), null));
        Thread.sleep(5000); // waiting for the socket setup.
        Assert.assertNotNull(player);
        Assert.assertNotNull(serverInformation);
        Assert.assertNotNull(gameRooms);
        Assert.assertNotNull(gameRoom);
    }

    @After
    public void tearDown(){
        try {
            userSigningModule.unregisterCallBack(this);
            roomListModule.unregisterCallBack(this);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(@NonNull Throwable err) {

    }

    @Override
    public void onSignInSuccessfully(@NonNull Player player) {
        this.player = player;
        GameRoom gameRoom = new GameRoom(GameMode.DUEL1A2B, "MyRoom", player);
        client.respond(protocolFactory.createProtocol("GetRooms", RequestStatus.request.toString(), null));
        client.respond(protocolFactory.createProtocol("CreateRoom", RequestStatus.request.toString(), gson.toJson(gameRoom)));
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onLoadServerInformation(ServerInformation serverInformation) {
        this.serverInformation = serverInformation;
    }

    @Override
    public void onGetRoomList(List<GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
        System.out.print(gameRooms);
    }

    @Override
    public void onNewRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    @Override
    public void onRoomClosed(GameRoom gameRoom) {

    }

    @Override
    public void onRoomUpdated(GameRoom gameRoom) {

    }

    @Override
    public void onJoinRoomSuccessfully(GameRoom gameRoom) {

    }
}
