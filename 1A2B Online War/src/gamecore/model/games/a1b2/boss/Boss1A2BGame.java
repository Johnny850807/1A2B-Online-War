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
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.NumberNotValidException;
import gamecore.model.games.a1b2.ProcessInvalidException;

/**
 * @author Waterball
 */
public class Boss1A2BGame extends Game{
	private Boss boss;
	private List<ClientPlayer> clientPlayers;
	private List<PlayerBlock> playerBlocks = Collections.synchronizedList(new ArrayList<>());
	private int whosTurn = 0; // the index from the clientPlayer showing who's turn.
	
	public Boss1A2BGame(ProtocolFactory protocolFactory, Boss boss, List<ClientPlayer> clientPlayers, String roomId) {
		super(protocolFactory, GameMode.BOSS1A2B, roomId);
		this.clientPlayers = clientPlayers;
		this.boss = boss;
	}
	
	@Override
	public void startGame() {
		super.startGame();
		boss.init();
	}
	
	public void attack(String playerId, String guess) throws NumberNotValidException{
		A1B2NumberValidator.validateNumber(guess);
		validateAttackingOperation(playerId);
		ClientPlayer player = getClientPlayer(playerId);
		boss.damage(player, guess);
		
		if (isAllPlayerTurnsOver())
			startBossAction();
		whosTurn = whosTurn + 1 > clientPlayers.size() ? 0 : whosTurn + 1;
		broadcastTurn();
	}

	private void validateAttackingOperation(String playerId) {
		ClientPlayer who = clientPlayers.get(whosTurn);
		if (!who.getId().equals(playerId))
			throw new ProcessInvalidException("Not your turn.");
	}
	
	private void startBossAction() {
		
	}
	
	private boolean isAllPlayerTurnsOver(){
		return whosTurn + 1 == clientPlayers.size();
	}
	
	public void broadcastTurn(){
		ClientPlayer who = clientPlayers.get(whosTurn);
		Protocol protocol = protocolFactory.createProtocol(YOUR_TURN, RequestStatus.success.toString(), null);
		who.broadcast(protocol);
	}
	
	public ClientPlayer getClientPlayer(String playerId){
		for (ClientPlayer player : clientPlayers)
			if (player.getId().equals(playerId))
				return player;
		throw new IllegalArgumentException("playerId " + playerId + " not exists");
	}
}
