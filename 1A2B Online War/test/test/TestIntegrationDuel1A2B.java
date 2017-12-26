package test;

import static container.Constants.Events.Chat.SEND_MSG;
import static container.Constants.Events.Games.Duel1A2B.GUESS;
import static container.Constants.Events.Games.Duel1A2B.GUESSING_STARTED;
import static container.Constants.Events.Games.Duel1A2B.ONE_ROUND_OVER;
import static container.Constants.Events.Games.Duel1A2B.SET_ANSWER;
import static container.Constants.Events.InRoom.CLOSE_ROOM;
import static container.Constants.Events.InRoom.LAUNCH_GAME;
import static container.Constants.Events.InRoom.LEAVE_ROOM;
import static container.Constants.Events.RoomList.CREATE_ROOM;
import static container.Constants.Events.RoomList.JOIN_ROOM;
import static container.Constants.Events.Signing.GETINFO;
import static container.Constants.Events.Signing.SIGNIN;
import static container.Constants.Events.Signing.SIGNOUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import container.Constants.Events.Games;
import container.base.Client;
import container.eventhandler.EventHandler;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;
import gamecore.model.ContentModel;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.RoomStatus;
import gamecore.model.ServerInformation;
import gamecore.model.games.Game;
import gamecore.model.games.a1b2.Duel1A2BGame;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;
import gamecore.model.games.a1b2.GameOverModel;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockClient;
import utils.RuntimeTypeAdapterFactory;

public class TestIntegrationDuel1A2B implements EventHandler.OnRespondingListener{
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
	protected int signInCount = 0;
	protected List<Duel1A2BPlayerBarModel> duelModels;
	
	@BeforeClass
	public static void BeforeClass(){
		System.setProperty("log4j.configurationFile","configuration.xml");
	}
	
	@Before
	public void setup(){
		RuntimeTypeAdapterFactory<Game> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
			    .of(Game.class, "gameMode")
			    .registerSubtype(Duel1A2BGame.class, "DUEL1A2B");

		gson = new GsonBuilder()
					.registerTypeAdapterFactory(runtimeTypeAdapterFactory)
					.create();
	}
	
	@Test
	public void test(){
		testSignIn();
		testCreateRoomAndJoin();
		testChatting();
		testPlayingDuel1A2B();
		//testPlayerLeft();
		testHostSignOut();  //select one in host sign out or close room
		//testCloseRoom();
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
		assertNotNull(this.gameRoom.getId());  //the game room should be initialized with id after handling.
		assertEquals(1, gamecore.getGameRooms().size());
		assertEquals(host, this.gameRoom.getHost());
		assertEquals(CREATE_ROOM, hostClient.getLastedResponse().getEvent());
		assertEquals(CREATE_ROOM, playerClient.getLastedResponse().getEvent());
		
		PlayerRoomIdModel joinRoomModel = new PlayerRoomIdModel(player.getId(), this.gameRoom.getId());
		createHandler(playerClient, protocolFactory.createProtocol(JOIN_ROOM, REQUEST, 
				gson.toJson(joinRoomModel))).handle();
		assertTrue(this.gameRoom.ifPlayerInStatusList(this.player));
		assertEquals(JOIN_ROOM, hostClient.getLastedResponse().getEvent());
		assertEquals(JOIN_ROOM, playerClient.getLastedResponse().getEvent());
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
	
	public void testPlayingDuel1A2B(){
		createHandler(hostClient, protocolFactory.createProtocol(LAUNCH_GAME, REQUEST, 
				gson.toJson(this.gameRoom))).handle();
		assertTrue(gameRoom.getRoomStatus() == RoomStatus.gamestarted);
		
		ContentModel hostSetAnswer = new ContentModel(host.getId(), gameRoom.getId(), "1234");
		ContentModel playerSetAnswer = new ContentModel(player.getId(), gameRoom.getId(), "5678");
		createHandler(hostClient, protocolFactory.createProtocol(SET_ANSWER, REQUEST, 
				gson.toJson(hostSetAnswer))).handle();
		assertEquals(SET_ANSWER,  hostClient.getLastedResponse().getEvent());
		createHandler(playerClient, protocolFactory.createProtocol(SET_ANSWER, REQUEST, 
				gson.toJson(playerSetAnswer))).handle();
		assertTrue(playerClient.hasReceivedEvent(SET_ANSWER));
		assertTrue(playerClient.hasReceivedEvent(GUESSING_STARTED));
		assertTrue(hostClient.hasReceivedEvent(GUESSING_STARTED));
		
		//first round guessing
		createHandler(hostClient, protocolFactory.createProtocol(GUESS, REQUEST, 
				gson.toJson(new ContentModel(host.getId(), gameRoom.getId(), "1234")))).handle();
		assertEquals(GUESS, hostClient.getLastedResponse().getEvent());
		createHandler(playerClient, protocolFactory.createProtocol(GUESS, REQUEST, 
				gson.toJson(new ContentModel(player.getId(), gameRoom.getId(), "5678")))).handle();
		assertTrue(playerClient.hasReceivedEvent(GUESS));;
		assertTrue(playerClient.hasReceivedEvent(ONE_ROUND_OVER));
		assertTrue(hostClient.hasReceivedEvent(ONE_ROUND_OVER));
		

		Type type = new TypeToken<ArrayList<Duel1A2BPlayerBarModel>>(){}.getType();
		duelModels = gson.fromJson(hostClient.getLastedResponse().getData(), type);
		assertNotNull(duelModels);
		assertEquals(1, duelModels.get(0).getGuessingTimes());
		
		//second round guessing
		createHandler(hostClient, protocolFactory.createProtocol(GUESS, REQUEST, 
				gson.toJson(new ContentModel(host.getId(), gameRoom.getId(), "5678")))).handle();
		assertEquals(GUESS, hostClient.getLastedResponse().getEvent());
		createHandler(playerClient, protocolFactory.createProtocol(GUESS, REQUEST, 
				gson.toJson(new ContentModel(player.getId(), gameRoom.getId(), "1234")))).handle();
		assertTrue(playerClient.hasReceivedEvent(GUESS));;
		assertTrue(playerClient.hasReceivedEvent(ONE_ROUND_OVER));
		assertTrue(hostClient.hasReceivedEvent(ONE_ROUND_OVER));
		
		assertTrue(playerClient.hasReceivedEvent(Games.GAMEOVER));
		assertTrue(hostClient.hasReceivedEvent(Games.GAMEOVER));
		
		GameOverModel gameOverModel = gson.fromJson(hostClient.getLastedResponse().getData(), GameOverModel.class);
		assertEquals(host.getId(), gameOverModel.getWinnerId());
	}
	
	public void testPlayerLeft(){
		createHandler(playerClient, protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())))).handle();
		assertEquals(1, gameRoom.getPlayerAmount());
		assertEquals(LEAVE_ROOM, hostClient.getLastedResponse().getEvent());
		assertEquals(player, gson.fromJson(hostClient.getLastedResponse().getData(), PlayerRoomModel.class).getPlayer());
	}
	
	public void testHostSignOut(){
		assertEquals(1, gamecore.getGameRooms().size());
		createHandler(hostClient, protocolFactory.createProtocol(SIGNOUT, REQUEST, 
				gson.toJson(host))).handle();
		assertTrue(host == null);
		assertEquals(0, gamecore.getGameRooms().size());
		if(playerClient.getLastedResponse().getEvent().equals(LEAVE_ROOM))
			assertEquals(CLOSE_ROOM, playerClient.getLastedResponse().getEvent());
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
			{
				this.host = player;
				this.hostClient.setId(player.getId());
			}
			else if (player.getName().equals("Player"))
			{
				this.player = player;
				this.playerClient.setId(player.getId());
			}
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
			Player joinPlayer = gson.fromJson(responseProtocol.getData(), PlayerRoomModel.class).getPlayer();
			assertEquals(this.player, joinPlayer);
			this.player.setUserStatus(ClientStatus.inRoom);
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
		case LEAVE_ROOM:
			PlayerRoomModel model = gson.fromJson(responseProtocol.getData(), PlayerRoomModel.class);
			this.gameRoom.removePlayer(model.getPlayer());
			assertEquals(this.player, model.getPlayer());
			assertEquals(this.gameRoom, model.getGameRoom());
			break;
		case SIGNOUT:
			host = null;
			break;
		case LAUNCH_GAME:
			System.out.println(responseProtocol.getData());
			this.gameRoom = gson.fromJson(responseProtocol.getData(), GameRoom.class);
			break;
		default:
			System.out.println("No Match: " + responseProtocol);
			break;
		}
			
	}
}
