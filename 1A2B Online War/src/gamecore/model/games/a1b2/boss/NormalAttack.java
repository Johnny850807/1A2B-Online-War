package gamecore.model.games.a1b2.boss;

import java.util.List;

import gamecore.model.games.a1b2.boss.AttackResult.AttackType;
import utils.RandomString;

/**
 * @author ShuYong
 */
public class NormalAttack extends AbstractMonsterAction{
	
	
	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getTargetPlayer(game);
		String guess = getGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.NORMAL);
		AttackActionModel model = new AttackActionModel(getCostMp(), monster);
		model.addAttackResult(attackResult);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		
		return 0;
	}


}
