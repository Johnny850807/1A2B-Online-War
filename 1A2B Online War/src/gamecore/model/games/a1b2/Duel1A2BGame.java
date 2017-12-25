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
import utils.ForServer;

/**
 * @author WaterBall
 * the model holding all the instances of one 1A2B-duel game.
 */
public class Duel1A2BGame extends Game{
	public static final int HOST = 0;
	public static final int PLAYER = 1;
	private ClientPlayer hostClient;
	private ClientPlayer playerClient;
	private Map<String, Duel1A2BPlayerBarModel> playerModels = new HashMap<>();  //<id, model>
	private ClientPlayer winner = null;
	private int guessingRound = 1;
	private boolean guessingStarted = false;
	
	@ForServer
	public Duel1A2BGame(ProtocolFactory protocolFactory, String roomId, ClientPlayer hostClient, ClientPlayer playerClient){
		super(protocolFactory, GameMode.DUEL1A2B, roomId);
		this.hostClient = hostClient;
		this.playerClient = playerClient;
		playerModels.put(hostClient.getId(), new Duel1A2BPlayerBarModel(hostClient.getId(), hostClient.getPlayerName()));
		playerModels.put(playerClient.getId(), new Duel1A2BPlayerBarModel(playerClient.getId(), playerClient.getPlayerName()));
	}
	
 	@ForServer
	public void commitPlayerAnswer(String playerId, String answer) throws NumberNotValidException{
		playerModels.get(playerId).setAnswer(answer);
		synchronized (playerModels) {
			log.trace("Room: " + roomId + ", Player: " + getPlayerName(playerId) + ", Set answer: " + answer);
			if (hasBothAnswersCommitted())
			{
				guessingStarted = true;
				broadcastGuessingStarted();
			}
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
		log.trace("Room: " + roomId + ", Guessing started.");
		broadcastToAll(protocol);
	}

 	@ForServer
	public void guess(String playerId, String guess) throws NumberNotValidException{
		if(!guessingStarted)
			log.error("Guessing has not started, but a player is guessing.");
		//because the action is the player guessing the answer of another player's, so first get the another model out.
		Duel1A2BPlayerBarModel anotherModel = getAnotherPlayerModel(playerId);
		GuessResult result = anotherModel.guess(guess);
		playerModels.get(playerId).addRecord(new GuessRecord(guess, result));
		log.trace("Room: " + roomId + ", Player: " + getPlayerName(playerId) + ", guess: " + guess + "(" + result + ")");
		
		synchronized (this) {
			if (result.getA() == 4 && winner == null)
			{
				log.trace("Room: " + roomId + ", The winner exists " + getPlayerName(playerId) + ".");
				winner = getClientPlayer(playerId);
			}
			if (isThisRoundOver())
				broadcastOneRoundOver();
		}
	}

	private boolean isThisRoundOver() {
		return playerModels.get(hostClient.getId()).getGuessingTimes() == this.guessingRound &&
				playerModels.get(playerClient.getId()).getGuessingTimes() == this.guessingRound;
	}
	
	private void broadcastOneRoundOver(){
		log.trace("Room: " + roomId + ",The " + guessingRound + " round is over. ");
		this.guessingRound ++;
		List<Duel1A2BPlayerBarModel> models = new ArrayList<>(playerModels.values());
		Protocol protocol = protocolFactory.createProtocol(Duel1A2B.ONE_ROUND_OVER,
				RequestStatus.success.toString(), gson.toJson(models));
		broadcastToAll(protocol);
		if (winner != null)
			broadcastWinnerEvent();
	}

	private void broadcastWinnerEvent() {
		log.trace("Room: " + roomId + ", The winner is " + winner.getPlayerName() + ", game over.");
		GameOverModel model = new GameOverModel(winner.getId(), roomId, gameMode, gameDuration);
		Protocol protocol = protocolFactory.createProtocol(Games.GAMEOVER, 
				RequestStatus.success.toString(), gson.toJson(model));
		broadcastToAll(protocol);
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
	
	@ForServer
	public boolean isGameOver(){
		return winner != null;
	}

	@ForServer
	public Player getWinner() {
		return winner.getPlayer();
	}

	@ForServer
	public void broadcastToAll(Protocol protocol){
		hostClient.broadcast(protocol);
		playerClient.broadcast(protocol);
		log.trace("Room: " + roomId + ", " + protocol.getEvent()+" event broadcasted.");
	}
	
	public Duel1A2BPlayerBarModel getPlayerBarModel(Player player){
		return playerModels.get(player.getId());
	}
	
	public List<GuessRecord> getGuessRecords(Player player){
		return playerModels.get(player.getId()).getGuessRecords();
	}
	
	public String getPlayerName(String playerId){
		return getClientPlayer(playerId).getPlayerName();
	}
}
