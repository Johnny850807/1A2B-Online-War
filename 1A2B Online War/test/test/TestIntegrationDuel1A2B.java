package test;

import static container.Constants.Events.Chat.SEND_MSG;
import static container.Constants.Events.Games.*;
import static container.Constants.Events.Games.Duel1A2B.GUESS;
import static container.Constants.Events.Games.Duel1A2B.GUESSING_STARTED;
import static container.Constants.Events.Games.Duel1A2B.ONE_ROUND_OVER;
import static container.Constants.Events.Games.Duel1A2B.SET_ANSWER;
import static container.Constants.Events.InRoom.*;
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
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import container.Constants.Events.Games;
import container.Constants.Events.InRoom;
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
import gamecore.model.games.a1b2.boss.base.AttackActionModel;
import gamecore.model.games.a1b2.boss.base.AttackResult;
import gamecore.model.games.a1b2.boss.base.NextTurnModel;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockClient;
import utils.DateDeserializer;
import utils.MyGson;
import utils.RuntimeTypeAdapterFactory;

public class TestIntegrationDuel1A2B implements EventHandler.OnRespondingListener{
	protected static final String REQUEST = RequestStatus.request.toString();
	protected GameFactory factory = new GameOnlineReleaseFactory();
	protected GameCore gamecore = factory.getGameCore();
	protected ProtocolFactory protocolFactory = factory.getProtocolFactory();
	protected GameEventHandlerFactory eventHandlerFactory = factory.getGameEventHandlerFactory();
	protected MockClient clientHost;
	protected Gson gson = MyGson.getGson();
	protected Player host = new Player("Host");
	protected Player player = new Player("Player");
	protected MockClient hostClient = new MockClient(); 
	protected MockClient playerClient = new MockClient();
	protected GameMode gameMode = GameMode.BOSS1A2B;
	protected GameRoom gameRoom;
	protected int signInCount = 0;
	protected List<Duel1A2BPlayerBarModel> duelModels;
	
	@BeforeClass
	public static void BeforeClass(){
		System.setProperty("log4j.configurationFile","configuration.xml");
	}
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void test(){
		long before = System.currentTimeMillis();
		testSignIn();
		testCreateRoomAndJoin();
		testChatting();
		
		/***choose only one of the testing method below alternatively.***/
		/***the game mode selected should be equal to the testing game method.***/
		
		//testPlayingDuel1A2B();  //if enable this, the game room will be closed after game completed
		testPlayingBoss1A2B();
		//testBootingPlayer();
		//testPlayerLeft();
		//testCloseRoom();
		
		testHostSignOut(); 
		long after = System.currentTimeMillis();
		System.out.println("Time cost: " + (after - before) + "ms");
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
		GameRoom room = new GameRoom(gameMode, "Game", host);
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

		bindPlayerAndHostFromUpdatedGameroom(gson.fromJson(hostClient.getLastedResponse().getData(), 
				PlayerRoomModel.class).getGameRoom());

		assertEquals(ClientStatus.inRoom, host.getUserStatus());
		assertEquals(ClientStatus.inRoom, player.getUserStatus());
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
	
	private void launchGameAndEnterGame() {
		createHandler(hostClient, protocolFactory.createProtocol(LAUNCH_GAME, REQUEST, 
				gson.toJson(this.gameRoom))).handle();
		assertTrue(gameRoom.getRoomStatus() == RoomStatus.gamestarted);
		
		createHandler(hostClient, protocolFactory.createProtocol(ENTERGAME, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(host.getId(), gameRoom.getId())))).handle();
		createHandler(playerClient, protocolFactory.createProtocol(ENTERGAME, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())))).handle();
		assertEquals(GAMESTARTED, hostClient.getLastedResponse().getEvent());
		assertEquals(GAMESTARTED, playerClient.getLastedResponse().getEvent());
	}
	
	public void testPlayingDuel1A2B(){
		launchGameAndEnterGame();
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
		
		assertEquals(GAMEOVER, playerClient.getLastedResponse().getEvent());
		assertEquals(GAMEOVER, hostClient.getLastedResponse().getEvent());
		assertEquals(0, factory.getGameCore().getGameRooms().size());
		/*bindPlayerAndHostFromUpdatedGameroom(gson.fromJson(hostClient.getLastedResponse().getData(), GameRoom.class));
		assertEquals(ClientStatus.signedIn, player.getUserStatus());
		assertEquals(ClientStatus.signedIn, host.getUserStatus());*/
		
		GameOverModel gameOverModel = gson.fromJson(hostClient.getLastedByEvent(GAMEOVER).getData(), GameOverModel.class);
		assertEquals(host.getId(), gameOverModel.getWinnerId());
	}
	
	private void testPlayingBoss1A2B() {
		launchGameAndEnterGame();
		
		//setting answer
		createHandler(hostClient, protocolFactory.createProtocol(Boss1A2B.SET_ANSWER,
				REQUEST, gson.toJson(new ContentModel(host.getId(), gameRoom.getId(), "1234")))).handle();
		assertEquals(Boss1A2B.SET_ANSWER,  hostClient.getLastedResponse().getEvent());
		createHandler(playerClient, protocolFactory.createProtocol(Boss1A2B.SET_ANSWER,
				REQUEST, gson.toJson(new ContentModel(player.getId(), gameRoom.getId(), "5678")))).handle();
		assertEquals(Boss1A2B.SET_ANSWER, playerClient.getLastedResponse().getEvent());
		
		//attacking started
		assertTrue(hostClient.hasReceivedEvent(Boss1A2B.ATTACKING_STARTED));
		assertTrue(playerClient.hasReceivedEvent(Boss1A2B.ATTACKING_STARTED));
		
		//host's turn
		validateNextTurn(hostClient.getLastedByEvent(Boss1A2B.NEXT_TURN), hostClient);
		validateNextTurn(playerClient.getLastedByEvent(Boss1A2B.NEXT_TURN), hostClient);
		
		createHandler(hostClient, protocolFactory.createProtocol(Boss1A2B.ATTACK,
				REQUEST, gson.toJson(new ContentModel(host.getId(), gameRoom.getId(), "1234")))).handle();
		validateLatestAttackResults("1234", host.getId(), hostClient);
		validateLatestAttackResults("1234", host.getId(), playerClient);
		
		//player's turn
		validateNextTurn(hostClient.getLastedByEvent(Boss1A2B.NEXT_TURN), playerClient);
		validateNextTurn(playerClient.getLastedByEvent(Boss1A2B.NEXT_TURN), playerClient);
		
		createHandler(playerClient, protocolFactory.createProtocol(Boss1A2B.ATTACK,
				REQUEST, gson.toJson(new ContentModel(player.getId(), gameRoom.getId(), "5678")))).handle();
		
		//the boss will attack any player after the player attacks
		validateLatestAttackerIsBoss(hostClient);
		validateLatestAttackerIsBoss(playerClient);
		
		//assert that the boss has attacked some one
		Protocol bossAttack = hostClient.getLastedByEvent(Boss1A2B.ATTACK_RESULTS);
		AttackActionModel attackActionModel = gson.fromJson(bossAttack.getData(), AttackActionModel.class);
		assertTrue(hasSomeoneGotHurted(attackActionModel));
		

		//host's turn
		validateNextTurn(hostClient.getLastedByEvent(Boss1A2B.NEXT_TURN), hostClient);
		validateNextTurn(playerClient.getLastedByEvent(Boss1A2B.NEXT_TURN), hostClient);
		
		//the player left
		createHandler(playerClient, protocolFactory.createProtocol(InRoom.LEAVE_ROOM,
				REQUEST, gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())))).handle();
		assertEquals(LEAVE_ROOM, hostClient.getLastedResponse().getEvent());
	}
	
	private boolean hasSomeoneGotHurted(AttackActionModel attackActionModel){
		for (AttackResult attackResult : attackActionModel)
			if (attackResult.getAttacked().getId().equals(host.getId()) ||
					attackResult.getAttacked().getId().equals(player.getId()))
				return true;
		return false;
	}
	
	private void validateLatestAttackResults(String expectedGuess, String attackerId, MockClient client){
		Protocol latestPtc = client.getLastedByEvent(Boss1A2B.ATTACK_RESULTS);
		AttackActionModel model = gson.fromJson(latestPtc.getData(), AttackActionModel.class);
		assertEquals(model.getAttackResults().get(0).getGuessRecord().getGuess(), expectedGuess);
		assertEquals(model.getAttacker().getId(), attackerId);
	}
	
	private void validateLatestAttackerIsBoss(MockClient client){
		Protocol latestPtc = client.getLastedByEvent(Boss1A2B.ATTACK_RESULTS);
		AttackActionModel model = gson.fromJson(latestPtc.getData(), AttackActionModel.class);
		assertEquals(model.getAttacker().getName().toLowerCase(), "boss");
	}
	
	private void validateNextTurn(Protocol protocol, Client whosTurn){
		NextTurnModel model = gson.fromJson(protocol.getData(), NextTurnModel.class);
		assertEquals(model.getWhosTurnId(), whosTurn.getId());
	}
	
	public void testBootingPlayer(){
		createHandler(hostClient, protocolFactory.createProtocol(BOOTED, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())))).handle();
		assertEquals(BOOTED, playerClient.getLastedResponse().getEvent());
		assertEquals(LEAVE_ROOM, hostClient.getLastedResponse().getEvent());
		player = gson.fromJson(playerClient.getLastedResponse().getData(), PlayerRoomModel.class).getPlayer();
		assertEquals(ClientStatus.inRoom, host.getUserStatus());
		assertEquals(ClientStatus.signedIn, player.getUserStatus());
	}
	
	public void testPlayerLeft(){
		createHandler(playerClient, protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())))).handle();
		assertEquals(1, gameRoom.getPlayerAmount());
		assertEquals(LEAVE_ROOM, hostClient.getLastedResponse().getEvent());
		assertTrue(!playerClient.getLastedResponse().getEvent().equals(LEAVE_ROOM));  // the player should not receive
		assertEquals(player, gson.fromJson(hostClient.getLastedResponse().getData(), PlayerRoomModel.class).getPlayer());
		assertEquals(ClientStatus.inRoom, host.getUserStatus());
		assertEquals(ClientStatus.signedIn, player.getUserStatus());
	}
	
	public void testHostSignOut(){
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
		bindPlayerAndHostFromUpdatedGameroom(gson.fromJson(hostClient.getLastedResponse().getData(), GameRoom.class));
		
		assertEquals(ClientStatus.signedIn, host.getUserStatus());
		assertEquals(ClientStatus.signedIn, player.getUserStatus());
	}
	
	private void bindPlayerAndHostFromUpdatedGameroom(GameRoom newRoom){
		gameRoom = newRoom;
		assertEquals(player, gameRoom.getPlayerStatus().get(0).getPlayer());
		assertEquals(host, gameRoom.getHost());
		player = gameRoom.getPlayerStatus().get(0).getPlayer();
		host = gameRoom.getHost();
	}
	
	protected EventHandler createHandler(Client client, Protocol protocol){
		System.out.println("Sending : " + protocol);
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
			this.player = model.getPlayer();
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
