package gamecore.model.games.a1b2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.Constants.Events.Games;
import container.Constants.Events.Games.Duel1A2B;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import gamecore.model.games.Game;

/**
 * @author WaterBall
 * the model holding all the instances of one 1A2B-duel game.
 */
public class Duel1A2BGame extends Game{
	private static transient Logger log = LogManager.getLogger(Duel1A2BGame.class);
	public static final int HOST = 0;
	public static final int PLAYER = 1;
	private ClientPlayer hostClient;
	private ClientPlayer playerClient;
	private Map<String, Duel1A2BPlayerBarModel> playerModels = new HashMap<>();  //<id, model>
	private ClientPlayer winner = null;
	private int guessingRound = 1;
	
	public Duel1A2BGame(ProtocolFactory protocolFactory, String roomId, ClientPlayer hostClient, ClientPlayer playerClient){
		super(protocolFactory, GameMode.DUEL1A2B, roomId);
		this.hostClient = hostClient;
		this.playerClient = playerClient;
		playerModels.put(hostClient.getId(), new Duel1A2BPlayerBarModel(hostClient.getId(), hostClient.getPlayerName()));
		playerModels.put(playerClient.getId(), new Duel1A2BPlayerBarModel(playerClient.getId(), playerClient.getPlayerName()));
	}
	
	public void commitPlayerAnswer(String playerId, String answer) throws NumberNotValidException{
		playerModels.get(playerId).setAnswer(answer);
		synchronized (playerModels) {
			log.info("PlayerId: " + playerId + ", Set answer: " + answer);
			if (hasBothAnswersCommitted())
				broadcastGuessingStarted();
		}
	}
	
	public boolean hasBothAnswersCommitted(){
		for (Duel1A2BPlayerBarModel model : playerModels.values())
			if (model.getAnswer() == null)
				return false;
		return true;
	}
	
	private void broadcastGuessingStarted() {
		Protocol protocol = protocolFactory.createProtocol(Duel1A2B.GUESSING_STARTED, 
				RequestStatus.success.toString(), null);
		hostClient.broadcast(protocol);
		playerClient.broadcast(protocol);
	}

	public void guess(String playerId, String guess) throws NumberNotValidException{
		//because the action is the player guessing the answer of another player's, so first get the another model out.
		Duel1A2BPlayerBarModel anotherModel = getAnotherPlayerModel(playerId);
		GuessResult result = anotherModel.guess(guess);
		playerModels.get(playerId).addRecord(new GuessRecord(guess, result));
		
		synchronized (this) {
			if (hasThisRoundOver())
				broadcastOneRoundOver();
			if (result.getA() == 4)
			{
				winner = getClientPlayer(playerId);
				broadcastWinnerEvent();
			}
		}
	}

	private boolean hasThisRoundOver() {
		return playerModels.get(hostClient.getId()).getGuessingTimes() == this.guessingRound &&
				playerModels.get(playerClient.getId()).getGuessingTimes() == this.guessingRound;
	}
	
	private void broadcastOneRoundOver(){
		this.guessingRound ++;
		List<Duel1A2BPlayerBarModel> models = new ArrayList<>(playerModels.values());
		Protocol protocol = protocolFactory.createProtocol(Duel1A2B.ONE_ROUND_OVER,
				RequestStatus.success.toString(), gson.toJson(models));
	}

	private void broadcastWinnerEvent() {
		GameOverModel model = new GameOverModel(winner.getId(), roomId, gameMode, gameDuration);
		Protocol protocol = protocolFactory.createProtocol(Games.GAMEOVER, 
				RequestStatus.success.toString(), gson.toJson(model));
		hostClient.broadcast(protocol);
		playerClient.broadcast(protocol);
	}


	public ClientPlayer getClientPlayer(String playerId){
		assert hostClient.getId().equals(playerId) || playerClient.getId().equals(playerId);
		return hostClient.getId().equals(playerId) ? hostClient : playerClient;
	}
	
	public String getAnotherPlayerId(String playerId){
		assert hostClient.getId().equals(playerId) || playerClient.getId().equals(playerId);
		return hostClient.getId().equals(playerId) ? playerClient.getId() : hostClient.getId();
	}
	
	public Duel1A2BPlayerBarModel getAnotherPlayerModel(String playerId){
		return playerModels.get(getAnotherPlayerId(playerId));
	}
	
	public boolean isGameOver(){
		return winner != null;
	}
	
	public Player getWinner() {
		return winner.getPlayer();
	}
	
	public Duel1A2BPlayerBarModel getPlayerBarModel(Player player){
		return playerModels.get(player.getId());
	}
	
	public List<GuessRecord> getGuessRecords(Player player){
		return playerModels.get(player.getId()).getGuessRecords();
	}
	
}
