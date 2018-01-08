package gamecore.model.games.a1b2.boss.core;

import java.util.List;

import container.protocol.Protocol;
import gamecore.model.games.a1b2.core.NumberNotValidException;

public interface IBoss1A2BGame {
	String getRoomId();
	
	void setPlayerAnswer(String playerId, String answer) throws NumberNotValidException;

	boolean areAllAnswersBeenCommitted();

	void attack(String playerId, String guess) throws NumberNotValidException;

	void addAllResultsAndbroadcastAttackActionModel(AttackActionModel model);

	void addAttackResult(AttackResult attackResult);

	/**
	 * find the next alive player's index from now whosTurn forward to the end.
	 * @return the next player's index, return -1 if all alive player turns are over.
	 * @exception IllegalStateException all players are dead
	 */
	int findNextAlivePlayerIndexForward();

	boolean isTheBossGameOver();

	boolean areAllPlayersDead();

	PlayerSpirit getPlayerSpirit(String playerId);

	List<PlayerSpirit> getPlayerSpirits();

	List<PlayerSpirit> getAlivePlayerSpirits();

	int getWhosTurn();

	Monster getBoss();

	void broadcastToAll(Protocol protocol);

}