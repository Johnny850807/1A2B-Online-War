package gamecore.model.games.a1b2.boss;

import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.boss.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.AttackResult.AttackType;

public class MagicAttackAction extends AbstractMonsterAction{

	
	
	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getTargetPlayer(game);
		String guess = getGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC);
		AttackActionModel model = new AttackActionModel(getCostMp(), monster);
		model.addAttackResult(attackResult);
		game.broadcastAttackActionModel(model);
		
	}

	@Override
	public int getCostMp() {
		return 100;
	}

}
