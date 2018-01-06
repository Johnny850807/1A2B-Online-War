package gamecore.model.games.a1b2.boss.imp;

import java.util.HashMap;
import java.util.Map;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.imp.PossibleTableGuessing;

/**
 * @author Waterball
 */
public class SmartGuessingAttack extends AbstractMonsterAction{
	/**
	 * <player's id, guessing algorithm>
	 * One guessing strategy handles one player.
	 */
	private Map<String, PossibleTableGuessing> guessStrategies = new HashMap<>();
	
	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		PlayerSpirit playerSpirit = getRandomTargetPlayer(game);
		if (!guessStrategies.containsKey(playerSpirit.getId()))
			guessStrategies.put(playerSpirit.getId(), new PossibleTableGuessing());
		String guess = guessStrategies.get(playerSpirit.getId()).nextGuess();
		AttackResult attackResult = playerSpirit.getAttacked(monster, guess, AttackType.NORMAL);
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster, attackResult);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 0;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.NORMAL;
	}

	@Override
	public String getAttackName() {
		return "Smart guess";
	}
}
