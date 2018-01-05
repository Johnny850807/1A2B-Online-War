package gamecore.model.games.a1b2.boss.imp;

import java.util.List;

import gamecore.model.games.a1b2.boss.base.AbstractMonsterAction;
import gamecore.model.games.a1b2.boss.base.AttackActionModel;
import gamecore.model.games.a1b2.boss.base.AttackResult;
import gamecore.model.games.a1b2.boss.base.Boss1A2BGame;
import gamecore.model.games.a1b2.boss.base.Monster;
import gamecore.model.games.a1b2.boss.base.PlayerSpirit;
import gamecore.model.games.a1b2.boss.base.AttackResult.AttackType;
import utils.RandomString;

/**
 * @author ShuYong
 */
public class RandomGuessAttack extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceRandomGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.NORMAL);
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster);
		model.addAttackResult(attackResult);
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
		return "Random guess";
	}
}
