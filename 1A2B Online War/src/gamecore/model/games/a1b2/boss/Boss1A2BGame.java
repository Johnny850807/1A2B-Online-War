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
	private int whosTurn = 0; // the index from the clientPlayer showing who's turn.
	
	public Boss1A2BGame(ProtocolFactory protocolFactory, Monster boss, List<ClientPlayer> clientPlayers, String roomId) {
		super(protocolFactory, GameMode.BOSS1A2B, roomId);
		this.boss = boss;
		for (ClientPlayer clientPlayer : clientPlayers)
			playerSpirits.add(createPlayerSpirit(clientPlayer));
	}
	
	@Override
	public void startGame() {
		super.startGame();
	}
	
	@ForServer
	public void attack(String playerId, String guess) throws NumberNotValidException{
		A1B2NumberValidator.validateNumber(guess);
		validateAttackingOperation(playerId);
		PlayerSpirit player = getPlayerSpirit(playerId);
		boss.damage(player, guess);
		
		if (isAllPlayerTurnsOver())
			startBossAction();
		whosTurn = whosTurn + 1 > playerSpirits.size() ? 0 : whosTurn + 1;
		broadcastTurn();
	}

	private void validateAttackingOperation(String playerId) {
		PlayerSpirit who = playerSpirits.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("Not your turn.");
	}
	
	private void startBossAction() {
		
	}
	
	private boolean isAllPlayerTurnsOver(){
		return whosTurn + 1 == playerSpirits.size();
	}
	
	private void broadcastTurn(){
		PlayerSpirit who = playerSpirits.get(whosTurn);
		Protocol protocol = protocolFactory.createProtocol(YOUR_TURN, RequestStatus.success.toString(), null);
		who.broadcast(protocol);
	}
	
	public PlayerSpirit getPlayerSpirit(String playerId){
		for (PlayerSpirit player : playerSpirits)
			if (player.getId().equals(playerId))
				return player;
		throw new IllegalArgumentException("playerId " + playerId + " not exists");
	}
	
	private PlayerSpirit createPlayerSpirit(ClientPlayer clientPlayer){
		return new PlayerSpirit(clientPlayer, log, protocolFactory);
	}

	@Override
	protected GameEnteringWaitingBox createEnteringWaitingBox() {
		ClientPlayer[] players = new ClientPlayer[playerSpirits.size()];
		for (int i = 0 ; i < playerSpirits.size() ; i ++)
			players[i] = playerSpirits.get(i).getPlayer();
		return new GameEnteringWaitingBox(this, players);
	}
}
