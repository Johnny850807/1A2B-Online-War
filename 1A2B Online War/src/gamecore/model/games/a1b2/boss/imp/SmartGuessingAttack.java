package gamecore.model.games.a1b2.boss.imp;

import java.util.HashMap;
import java.util.Map;

import gamecore.model.games.a1b2.PossibleTableGuessing;
import gamecore.model.games.a1b2.boss.base.AbstractMonsterAction;
import gamecore.model.games.a1b2.boss.base.AttackActionModel;
import gamecore.model.games.a1b2.boss.base.AttackResult;
import gamecore.model.games.a1b2.boss.base.Boss1A2BGame;
import gamecore.model.games.a1b2.boss.base.Monster;
import gamecore.model.games.a1b2.boss.base.PlayerSpirit;
import gamecore.model.games.a1b2.boss.base.AttackResult.AttackType;

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
	public void execute(Monster monster, Boss1A2BGame game) {
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
