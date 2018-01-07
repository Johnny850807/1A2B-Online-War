package gamecore.model.games.a1b2.boss.imp;

import static container.core.Constants.Events.Games.Boss1A2B.NEXT_TURN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import container.core.Constants.Events.Games;
import container.core.Constants.Events.Games.Boss1A2B;
import container.eventhandler.handlers.games.boss1a2b.SetAnswerHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.ClientPlayer;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import gamecore.model.games.Game;
import gamecore.model.games.GameOverModel;
import gamecore.model.games.ProcessInvalidException;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.NextTurnModel;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.SpiritsModel;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.NumberNotValidException;
import utils.ForServer;

/**
 * @author Waterball
 */
public class Boss1A2BGame extends Game implements IBoss1A2BGame{
	private transient Monster boss;
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
	protected void onInitGame() {
		boss.init(this);
	}
	
	@Override
	protected Protocol onCreateGameStartedProtocol() {
		return protocolFactory.createProtocol(Games.GAMESTARTED, RequestStatus.success.toString(),
				gson.toJson(new SpiritsModel(boss, playerSpirits)));
	}
	
	@Override
	@ForServer
	public synchronized void setPlayerAnswer(String playerId, String answer) throws NumberNotValidException{
		validateSettingAnswerOperation(answer);
		PlayerSpirit playerSpirit = getPlayerSpirit(playerId);
		playerSpirit.setAnswer(answer);
		
		if (areAllAnswersBeenCommitted())
		{
			startAttackingAndBroadcast();
			broadcastNextTurn(whosTurn);
		}
	}
	
	private void validateSettingAnswerOperation(String answer) throws NumberNotValidException{
		validateGameStarted();
		A1B2NumberValidator.validateNumber(answer);
		if (attackingStarted)
			throw new ProcessInvalidException("The attacking phase is started, you cannot set the answer anymore.");
	}
	
	@Override
	@ForServer
	public boolean areAllAnswersBeenCommitted(){
		if (boss.getAnswer() == null)
			throw new IllegalStateException("The boss has not committed the answer, why?");
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			if (playerSpirit.getAnswer() == null)
				return false;
		return true;
	}
	
	private void startAttackingAndBroadcast(){
		attackingStarted = true;
		Protocol protocol = protocolFactory.createProtocol(Boss1A2B.ATTACKING_STARTED,
				RequestStatus.success.toString(), null);
		broadcastToAll(protocol);
	}
	

	@Override
	@ForServer
	public synchronized void attack(String playerId, String guess) throws NumberNotValidException{
		validateAttackingOperation(playerId, guess);
		PlayerSpirit attacker = getPlayerSpirit(playerId);
		attackingBoss(attacker, guess);
		log.info(getPlayerSpiritsStatus());
		
		if (boss.isDead())
			broadcastGameOver();
		else
		{
			whosTurn = findNextAlivePlayerIndexForward();
			if (whosTurn != -1) //if any next player's turn has not been over
				broadcastNextTurn(whosTurn);
			else 
			{
				delay(2000);
				boss.action();
				log.info(getPlayerSpiritsStatus());
				if (areAllPlayersDead())
					broadcastGameOver();
				else
				{
					whosTurn = findNextAlivePlayerIndexForward();  //find the next alive player from -1
					broadcastNextTurn(whosTurn);
				}
			}
		}
	}

	private void validateAttackingOperation(String playerId, String guess) throws NumberNotValidException{
		validateGameStarted();
		PlayerSpirit who = playerSpirits.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("The turn now is " + whosTurn + ", not your turn.");
		A1B2NumberValidator.validateNumber(guess);
	}
	
	private void attackingBoss(PlayerSpirit attacker, String guess){
		log.trace("the player "+ attacker.getName() + " is attacking the boss.");
		AttackResult attackResult = boss.getAttacked(attacker, guess, AttackType.NORMAL);
		AttackActionModel actionModel = new AttackActionModel("Normal guess", 0, attacker, attackResult);
		addAllResultsAndbroadcastAttackActionModel(actionModel);
	}

	@Override
	@ForServer
	public void addAllResultsAndbroadcastAttackActionModel(AttackActionModel model){
		log.trace("Adding the attack action: " + model);
		for (AttackResult attackResult : model)
			addAttackResult(attackResult);
		Protocol protocol = protocolFactory.createProtocol(Boss1A2B.ATTACK_RESULTS,
				RequestStatus.success.toString(), gson.toJson(model));
		broadcastToAll(protocol);
	}
	

	@Override
	@ForServer
	public void addAttackResult(AttackResult attackResult){
		this.attackResults.add(attackResult);
	}


	@Override
	public int findNextAlivePlayerIndexForward(){
		if (areAllPlayersDead())
			throw new IllegalStateException("All players are dead, why should we find out the next alive player index?");
		int index = whosTurn;
		do {
			if (++index >= playerSpirits.size())
				return -1;
		} while (playerSpirits.get(index).isDead());
		return index;
	}
	

	@Override
	public boolean isTheBossGameOver(){
		return boss.isDead() || areAllPlayersDead() ;
	}
	

	@Override
	public boolean areAllPlayersDead(){
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			if (!playerSpirit.isDead())
				return false;
		return true;
	}
	

	@Override
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
	
	private void broadcastNextTurn(int nextTurn){
		delay(800);
		log.trace(getPlayerSpirits().get(nextTurn).getName() + "'s turn.");
		PlayerSpirit who = playerSpirits.get(nextTurn);
		Protocol protocol = protocolFactory.createProtocol(NEXT_TURN, RequestStatus.success.toString(), 
				gson.toJson(new NextTurnModel(who)));
		broadcastToAll(protocol);
	}
	

	@Override
	public List<PlayerSpirit> getPlayerSpirits() {
		return playerSpirits;
	}
	

	@Override
	public List<PlayerSpirit> getAlivePlayerSpirits() {
		List<PlayerSpirit> alivePlayerSpirits = new ArrayList<>();
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			if (!playerSpirit.isDead())
				alivePlayerSpirits.add(playerSpirit);
		return alivePlayerSpirits;
	} 
	
	

	@Override
	public int getWhosTurn() {
		return whosTurn;
	}
	
	@Override
	@ForServer
	public Monster getBoss() {
		return boss;
	}

	@Override
	public void broadcastToAll(Protocol protocol){
		for (PlayerSpirit player : playerSpirits)
			player.broadcast(protocol);
	}
	
	private String getPlayerSpiritsStatus(){
		StringBuilder strb = new StringBuilder();
		strb.append("========Status========\n");
		for (PlayerSpirit playerSpirit : getPlayerSpirits())
			strb.append(playerSpirit).append("\n");
		strb.append(boss).append("\n");
		strb.append("\n========Status========");
		return strb.toString();
	}
	
	private void delay(long delay){
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
