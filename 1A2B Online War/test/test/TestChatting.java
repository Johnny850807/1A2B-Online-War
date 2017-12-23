package test;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import static container.Constants.Events.Signing.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.Chat.*;

import container.Constants.Events.Chat;
import container.Constants.Events.RoomList;
import container.base.Client;
import container.eventhandler.EventHandler;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.JoinRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockClient;
import static org.junit.Assert.*;

import java.awt.Frame;
import java.io.Reader;
import java.util.List;

public class TestChatting implements EventHandler.OnRespondingListener{
	protected static final String REQUEST = RequestStatus.request.toString();
	protected GameFactory factory = new GameOnlineReleaseFactory();
	protected GameCore gamecore = factory.getGameCore();
	protected ProtocolFactory protocolFactory = factory.getProtocolFactory();
	protected GameEventHandlerFactory eventHandlerFactory = factory.getGameEventHandlerFactory();
	protected MockClient clientHost;
	protected Gson gson = new Gson();
	protected Player host = new Player("Host");
	protected Player player = new Player("Player");
	protected MockClient hostClient = new MockClient(); 
	protected MockClient playerClient = new MockClient();
	protected GameRoom gameRoom;
	private int signInCount = 0;
	
	@Test
	public void test(){
		testSignIn();
		testCreateRoomAndJoin();
		testChatting();
		testCloseRoom();
	}
	
	public void testSignIn(){
		createHandler(hostClient, protocolFactory.createProtocol(SIGNIN, 
				REQUEST, gson.toJson(host))).handle();
		assertTrue(hostClient.getLastedResponse().getEvent().equals(SIGNIN));
		createHandler(playerClient, protocolFactory.createProtocol(SIGNIN, 
				REQUEST, gson.toJson(player))).handle();
		assertTrue(playerClient.getLastedResponse().getEvent().equals(SIGNIN));
		assertEquals(2, signInCount);
	}
	
	public void testCreateRoomAndJoin(){
		GameRoom room = new GameRoom(GameMode.DUEL1A2B, "Game", host);
		createHandler(hostClient, protocolFactory.createProtocol(CREATE_ROOM, REQUEST, 
				gson.toJson(room))).handle();
		assertNotNull(this.gameRoom);  //the game room should be init with id after handling.
		assertNotNull(this.gameRoom.getId()); 
		assertEquals(1, gamecore.getGameRooms().size());
		JoinRoomModel joinRoomModel = new JoinRoomModel(player.getId(), this.gameRoom.getId());
		createHandler(playerClient, protocolFactory.createProtocol(JOIN_ROOM, REQUEST, 
				gson.toJson(joinRoomModel))).handle();
		assertTrue(this.gameRoom.getHost().equals(this.host));
		assertTrue(this.gameRoom.ifPlayerInStatusList(this.player));
	}
	
	public void testChatting(){
		String[] msgs = {"Hi.", "yo!", "let's get started!"};
		ChatMessage[] chatMessages = {new ChatMessage(gameRoom, host, msgs[0]), 
				new ChatMessage(gameRoom, player, msgs[1]), new ChatMessage(gameRoom, host, msgs[2])};
		createHandler(hostClient, protocolFactory.createProtocol(SEND_MSG, REQUEST, 
				gson.toJson(chatMessages[0]))).handle();
		createHandler(playerClient, protocolFactory.createProtocol(SEND_MSG, REQUEST, 
				gson.toJson(chatMessages[1]))).handle();
		createHandler(hostClient, protocolFactory.createProtocol(SEND_MSG, REQUEST, 
				gson.toJson(chatMessages[2]))).handle();
		assertTrue(gameRoom.getChatMessagesSize() == msgs.length);
		assertAllChatmessageContentsEqual(chatMessages, gameRoom.getChatMessageList());
		
		//check if the clients received the msgs
		assertEquals(3, hostClient.getReceivedMessages().size());
		assertEquals(3, playerClient.getReceivedMessages().size());
	}
	
	private void assertAllChatmessageContentsEqual(ChatMessage[] expecteds, List<ChatMessage> actuals){
		for (int i = 0 ; i < expecteds.length ; i ++)
			assertEquals(expecteds[i].getContent(), actuals.get(i).getContent());
	}
	
	public void testCloseRoom(){
		createHandler(hostClient, protocolFactory.createProtocol(CLOSE_ROOM, REQUEST, 
				gson.toJson(this.gameRoom))).handle();
		assertEquals(CLOSE_ROOM, hostClient.getLastedResponse().getEvent());
		assertEquals(CLOSE_ROOM, playerClient.getLastedResponse().getEvent());
	}
	
	
	protected EventHandler createHandler(Client client, Protocol protocol){
		EventHandler handler = eventHandlerFactory.createGameEventHandler(client, protocol);
		handler.setOnRespondingListener(this);
		return handler;
	}

	@Override
	public void onErrorResponding(Protocol responseProtocol) {
		System.out.println(responseProtocol);
		Assert.fail();
	}

	@Override
	public void onSuccessResponding(Protocol responseProtocol) {
		String event = responseProtocol.getEvent();
		switch (event) {
		case SIGNIN:
			Player player = gson.fromJson(responseProtocol.getData(), Player.class);
			if(player.getName().equals("Host"))
				this.host = player;
			else if (player.getName().equals("Player"))
				this.player = player;
			signInCount ++;
			break;
		case GETINFO:
			ServerInformation info = gson.fromJson(responseProtocol.getData(), ServerInformation.class);
			System.out.println(info);
			break;
		case CREATE_ROOM:
			this.gameRoom = gson.fromJson(responseProtocol.getData(), GameRoom.class);
			System.out.println(gameRoom);
			break;
		case JOIN_ROOM:
			Player joinPlayer = gson.fromJson(responseProtocol.getData(), Player.class);
			assertEquals(this.player, joinPlayer);
			this.gameRoom.addPlayer(joinPlayer);
			assertTrue(this.gameRoom.getPlayerAmount() == 2);
			break;
		case SEND_MSG:
			ChatMessage message = gson.fromJson(responseProtocol.getData(), ChatMessage.class);
			this.gameRoom.addChatMessage(message);
			break;
		case CLOSE_ROOM:
			GameRoom closedRoom = gson.fromJson(responseProtocol.getData(), GameRoom.class);
			assertEquals(this.gameRoom, closedRoom);
			this.gameRoom = null;
			break;
		default:
			System.out.println("No Match: " + responseProtocol);
			fail();
			break;
		}
			
	}
}
