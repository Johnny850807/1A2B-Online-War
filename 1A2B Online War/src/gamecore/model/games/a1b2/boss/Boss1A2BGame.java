package gamecore.model.games.a1b2.boss;

import static container.Constants.Events.Games.Boss1A2B.NEXT_TURN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import container.Constants.Events.Games.Boss1A2B;
import container.eventhandler.handlers.games.boss1a2b.SetAnswerHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.ClientPlayer;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import gamecore.model.games.Game;
import gamecore.model.games.ProcessInvalidException;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.NumberNotValidException;
import gamecore.model.games.a1b2.boss.AttackResult.AttackType;
import utils.ForServer;

/**
 * @author Waterball
 */
public class Boss1A2BGame extends Game{
	private Monster boss;
	private List<PlayerSpirit> playerSpirits = Collections.synchronizedList(new ArrayList<>());
	private List<AttackResult> attackResults = new ArrayList<>();
	private int whosTurn = 0; //the index from the clientPlayer showing who's turn.
	private boolean attackingStarted = false;
	
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
	public void setPlayerAnswer(String playerId, String answer) throws NumberNotValidException{
		validateSetAnswerOperation(answer);
		PlayerSpirit playerSpirit = getPlayerSpirit(playerId);
		playerSpirit.setAnswer(answer);
		
		if (hasAllAnswerBeenCommitted())
			startAttackingAndBroadcast();
	}
	
	@ForServer
	public boolean hasAllAnswerBeenCommitted(){
		if (boss.getAnswer() == null)
			throw new IllegalStateException("The boss has not committed the answer, why?");
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			if (playerSpirit.getAnswer() == null)
				return false;
		return true;
	}
	
	private void validateSetAnswerOperation(String answer) throws NumberNotValidException{
		validateGameStarted();
		A1B2NumberValidator.validateNumber(answer);
		if (attackingStarted)
			throw new ProcessInvalidException("The attacking phase is started, you cannot set the answer anymore.");
	}
	
	private void startAttackingAndBroadcast(){
		attackingStarted = true;
		Protocol protocol = protocolFactory.createProtocol(Boss1A2B.ATTACKING_STARTED,
				RequestStatus.success.toString(), null);
		broadcastToAll(protocol);
	}
	
	@ForServer
	public void attack(String playerId, String guess) throws NumberNotValidException{
		validateAttackingOperation(playerId);
		A1B2NumberValidator.validateNumber(guess);
		PlayerSpirit attacker = getPlayerSpirit(playerId);
		AttackResult attackResult = boss.getAttacked(attacker, guess, AttackType.NORMAL);
		attackResults.add(attackResult);
		
		if (allPlayerTurnsOver())
			boss.action();
		whosTurn = whosTurn + 1 > playerSpirits.size() ? 0 : whosTurn + 1;
		if (isTheGameOver())
			broadcastGameOver();
		else 
			broadcastNextTurn();
	}

	private void validateAttackingOperation(String playerId) {
		validateGameStarted();
		PlayerSpirit who = playerSpirits.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("Not your turn.");
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
	
	private boolean isTheGameOver(){
		if (boss.isDead())
			return true;
		for (PlayerSpirit playerSpirit : playerSpirits)
			if (playerSpirit.isDead())
				return true;
		return false;
	}
	
	public PlayerSpirit getPlayerSpirit(String playerId){
		for (PlayerSpirit player : playerSpirits)
			if (player.getId().equals(playerId))
				return player;
		throw new IllegalArgumentException("playerId " + playerId + " not exists");
	}

	private void broadcastGameOver(){
		
	}
	
	public void broadcastToAll(Protocol protocol){
		for (PlayerSpirit player : playerSpirits)
			player.broadcast(protocol);
	}

	@ForServer
	public void addAttackResult(AttackResult attackResult){
		this.attackResults.add(attackResult);
	}
	
	@ForServer
	public void broadcastAttackActionModel(AttackActionModel model){
		for (AttackResult attackResult : model)
			addAttackResult(attackResult);
		//TODO broadcast
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
