package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import container.core.Constants.Events.Games;
import container.core.Constants.Events.Games.Boss1A2B;
import container.protocol.Protocol;

import static org.junit.Assert.*;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.GameMode;
import gamecore.model.MockLogger;
import gamecore.model.games.GameEnteringWaitingBox;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.ProcessInvalidException;
import gamecore.model.games.Game;
import gamecore.model.games.Game.GameLifecycleListener;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.imp.Boss1A2BGame;
import gamecore.model.games.a1b2.boss.imp.OnePunchBoss;
import gamecore.model.games.a1b2.boss.imp.TestingBoss;
import gamecore.model.games.a1b2.core.NumberNotValidException;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;
import mock.MockClient;
import utils.MyGson;

@RunWith(Parameterized.class)
public class TestBoss1a2bGame implements GameLifecycleListener{
	private static GameFactory gameFactory;
	private Boss1A2BGame game;
	private Monster boss;
	private GameRoom room;
	private Player host;
	private Player player;
	private MockClient hostClient;
	private MockClient playerClient;
	private List<ClientPlayer> players;
	
	@BeforeClass
	public static void BeforeClass(){
		System.setProperty("log4j.configurationFile","configuration.xml");
		gameFactory = new GameOnlineReleaseFactory();
	}
	
	public TestBoss1a2bGame(Monster boss) {
		this.boss = boss;
	}
	
	@Before
	public void setup(){
		host = new Player("host");
		player = new Player("player");
		hostClient = new MockClient();
		playerClient = new MockClient();
		players = new ArrayList<>();
		host.setId(hostClient.getId());
		player.setId(playerClient.getId());
		players.add(new ClientPlayer(hostClient, host));
		players.add(new ClientPlayer(playerClient, player));
		room = new GameRoom(GameMode.BOSS1A2B, "Room", host);
		room.initId();
		game = new Boss1A2BGame(gameFactory.getProtocolFactory(), boss, players, room.getId());
		game.setEnteringWaitingBox(new GameEnteringWaitingBox(game, players));
		game.setGameLifecycleListener(this);
	}
	
	@Test
	public void testFightingBoss() throws NumberNotValidException{
		game.enterGame(players.get(0));
		game.enterGame(players.get(1));
		game.setPlayerAnswer(host.getId(), "1234");
		game.setPlayerAnswer(player.getId(), "1234");
		fightingLoop:
		do {
			for (ClientPlayer player : players)
			{
				if (hostClient.hasReceivedEvent(Games.GAMEOVER))
					break fightingLoop;
				if (!game.getPlayerSpirit(player.getId()).isDead())
					game.attack(player.getId(), boss.getAnswer());
			}
		} while (!hostClient.hasReceivedEvent(Games.GAMEOVER));
		
		Protocol gameOver = hostClient.getLastedByEvent(Games.GAMEOVER);
		String expectedResult = (boss instanceof OnePunchBoss ? Boss1A2B.WinnerId.BOSS_WIN : Boss1A2B.WinnerId.PLAYERS_WIN);
		GameOverModel model = MyGson.parse(gameOver.getData(), GameOverModel.class);
		assertEquals(model.getWinnerId(), expectedResult);
	}

	@Test(expected=ProcessInvalidException.class)
	public void testAttackInvalid() throws NumberNotValidException{
		game.enterGame(players.get(0));
		game.enterGame(players.get(1));
		game.attack(player.getId(), "1234");
	}
	
	
	@Override
	public void onGameStarted(Game game) {
		assertEquals(this.game, game);
	}

	@Override
	public void onGameInterrupted(Game game, ClientPlayer noResponsePlayer) {}

	@Override
	public void onGameOver(Game game, GameOverModel gameOverModel) {
	}
	
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		gameFactory = new GameOnlineReleaseFactory();
		return Arrays.asList(new Object[][] {
			{new TestingBoss("t", "TestingBoss", new MockLogger(), gameFactory.getProtocolFactory())},
			{new OnePunchBoss("o", "OnePunchBoss", new MockLogger(), gameFactory.getProtocolFactory())}
	      });
	}
}
