package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static container.Constants.Events.Games.Boss1A2B.*;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.ClientPlayer;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import gamecore.model.games.Game;
import gamecore.model.games.GameEnteringWaitingBox;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.NumberNotValidException;
import gamecore.model.games.a1b2.ProcessInvalidException;
import utils.ForServer;

/**
 * @author Waterball
 */
public class Boss1A2BGame extends Game{
	private Monster boss;
	private List<PlayerSpirit> playerSpirits = Collections.synchronizedList(new ArrayList<>());
	private List<AttackResult> attackResults = new ArrayList<>();
	private int whosTurn = 0; // the index from the clientPlayer showing who's turn.
	
	public Boss1A2BGame(ProtocolFactory protocolFactory, Monster boss, List<ClientPlayer> clientPlayers, String roomId) {
		super(protocolFactory, GameMode.BOSS1A2B, roomId);
		this.boss = boss;
		for (ClientPlayer clientPlayer : clientPlayers)
			playerSpirits.add(new PlayerSpirit(clientPlayer, log, protocolFactory));
	}
	
	@Override
	public void startGame() {
		super.startGame();
		boss.init(this);
	}
	
	@ForServer
	public void attack(String playerId, String guess) throws NumberNotValidException{
		validateAttackingOperation(playerId);
		A1B2NumberValidator.validateNumber(guess);
		PlayerSpirit attacker = getPlayerSpirit(playerId);
		AttackResult attackResult = boss.attack(attacker, guess);
		attackResults.add(attackResult);
		
		if (allPlayerTurnsOver())
			boss.action();
		whosTurn = whosTurn + 1 > playerSpirits.size() ? 0 : whosTurn + 1;
		broadcastNextTurn();
	}

	private void validateAttackingOperation(String playerId) {
		validGameStarted();
		PlayerSpirit who = playerSpirits.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("Not your turn.");
	}
	
	private void startBossAction() {
		
	}
	
	private boolean allPlayerTurnsOver(){
		return whosTurn == playerSpirits.size() - 1;
	}
	
	private void broadcastNextTurn(){
		PlayerSpirit who = playerSpirits.get(whosTurn);
		Protocol protocol = protocolFactory.createProtocol(NEXT_TURN, RequestStatus.success.toString(), 
				gson.toJson(who.getClientPlayer().getPlayer()));
		broadcastToAll(protocol);
	}
	
	public PlayerSpirit getPlayerSpirit(String playerId){
		for (PlayerSpirit player : playerSpirits)
			if (player.getId().equals(playerId))
				return player;
		throw new IllegalArgumentException("playerId " + playerId + " not exists");
	}

	public void broadcastToAll(Protocol protocol){
		for (PlayerSpirit player : playerSpirits)
			player.broadcast(protocol);
	}

	@ForServer
	public void addAttackResult(AttackResult attackResult){
		this.attackResults.add(attackResult);
	}
	
	public void removeAttackResult(AttackResult attackResult){
		this.attackResults.remove(attackResult);
	}
	
	public List<PlayerSpirit> getPlayerSpirits() {
		return playerSpirits;
	}
	
	public int getWhosTurn() {
		return whosTurn;
	}
	
	@ForServer
	public Monster getBoss() {
		return boss;
	}
}
