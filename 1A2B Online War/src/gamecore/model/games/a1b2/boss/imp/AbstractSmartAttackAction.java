package gamecore.model.games.a1b2.boss.imp;

import java.util.HashMap;
import java.util.Map;

import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.imp.PossibleTableGuessing;

public abstract class AbstractSmartAttackAction extends AbstractMonsterAction{
	/**
	 * <player's id, guessing algorithm>
	 * One guessing strategy handles one player.
	 */
	protected Map<String, PossibleTableGuessing> guessStrategies = new HashMap<>();
	
	public String produceSmartGuess(PlayerSpirit playerSpirit){
		if (!guessStrategies.containsKey(playerSpirit.getId()))
			guessStrategies.put(playerSpirit.getId(), new PossibleTableGuessing());
		return guessStrategies.get(playerSpirit.getId()).nextGuess();
	}
	
	public void feedGuessRecord(PlayerSpirit playerSpirit, GuessRecord guessRecord){
		if (!guessStrategies.containsKey(playerSpirit.getId()))
			guessStrategies.put(playerSpirit.getId(), new PossibleTableGuessing());
		guessStrategies.get(playerSpirit.getId()).feedRecord(guessRecord);
	}
}
