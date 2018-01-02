package gamecore.model.games.a1b2.boss;

import static container.Constants.Events.Games.Boss1A2B.NEXT_TURN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import container.Constants.Events.Games;
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
import gamecore.model.games.a1b2.GameOverModel;
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
	public synchronized void setPlayerAnswer(String playerId, String answer) throws NumberNotValidException{
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
	public synchronized void attack(String playerId, String guess) throws NumberNotValidException{
		validateAttackingOperation(playerId, guess);
		PlayerSpirit attacker = getPlayerSpirit(playerId);
		handleAttacking(attacker, guess);
		
		if (boss.isDead())
			broadcastGameOver();
		else
		{
			if (allPlayerTurnsOver())
			{
				boss.action();
				if (allPlayersAreDead())
					broadcastGameOver();
				else
					switchIndexToNextAndBroadcastNextTurn();
			}
			else
				switchIndexToNextAndBroadcastNextTurn();
		}
	}

	private void validateAttackingOperation(String playerId, String guess) throws NumberNotValidException{
		validateGameStarted();
		PlayerSpirit who = playerSpirits.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("Not your turn.");
		A1B2NumberValidator.validateNumber(guess);
	}
	
	private void handleAttacking(PlayerSpirit attacker, String guess){
		AttackResult attackResult = boss.getAttacked(attacker, guess, AttackType.NORMAL);
		AttackActionModel actionModel = new AttackActionModel(0, attacker, attackResult);
		log.trace("Attack action: " + actionModel);
		addAllResultsAndbroadcastAttackActionModel(actionModel);
	}
	
	private void switchIndexToNextAndBroadcastNextTurn(){
		this.whosTurn = this.whosTurn + 1 > playerSpirits.size() ? 0 : this.whosTurn + 1;
		PlayerSpirit who = playerSpirits.get(whosTurn);
		Protocol protocol = protocolFactory.createProtocol(NEXT_TURN, RequestStatus.success.toString(), 
				gson.toJson(who.getClientPlayer().getPlayer()));
		broadcastToAll(protocol);
	}
	
	private boolean allPlayerTurnsOver(){
		return whosTurn == playerSpirits.size() - 1;
	}

	public boolean isTheBossGameOver(){
		return boss.isDead() || allPlayersAreDead() ;
	}
	
	public boolean allPlayersAreDead(){
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			if (!playerSpirit.isDead())
				return false;
		return true;
	}
	
	public PlayerSpirit getPlayerSpirit(String playerId){
		for (PlayerSpirit player : playerSpirits)
			if (player.getId().equals(playerId))
				return player;
		throw new IllegalArgumentException("playerId " + playerId + " not exists");
	}

	private void broadcastGameOver(){
		String winnerId = boss.isDead() ? Boss1A2B.WinnerId.PLAYERS_WIN : Boss1A2B.WinnerId.BOSS_WIN;
		GameOverModel gameOverModel = new GameOverModel(winnerId, roomId, gameMode, gameDuration);
		Protocol protocol = protocolFactory.createProtocol(Games.GAMEOVER, RequestStatus.success.toString(),
				gson.toJson(gameOverModel));
		broadcastToAll(protocol);
	}

	@ForServer
	public void addAttackResult(AttackResult attackResult){
		this.attackResults.add(attackResult);
	}
	
	@ForServer
	public void addAllResultsAndbroadcastAttackActionModel(AttackActionModel model){
		for (AttackResult attackResult : model)
			addAttackResult(attackResult);
		Protocol protocol = protocolFactory.createProtocol(Boss1A2B.ATTACK_RESULTS,
				RequestStatus.success.toString(), gson.toJson(model));
		broadcastToAll(protocol);
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

	public void broadcastToAll(Protocol protocol){
		for (PlayerSpirit player : playerSpirits)
			player.broadcast(protocol);
	}
}
