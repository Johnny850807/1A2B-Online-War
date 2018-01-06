package gamecore.model.games.a1b2.boss.imp;

import java.util.List;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import utils.RandomString;

/**
 * @author ShuYong
 */
public class RandomGuessAttack extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
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
