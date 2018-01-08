package gamecore.model.games.a1b2.boss.imp;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;

public class ExplosionAttack extends AbstractSmartAttackAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster);
		for (PlayerSpirit playerSpirit : game.getAlivePlayerSpirits())
		{
			String guess = produceSmartGuess(playerSpirit);
			AttackResult attackResult = playerSpirit.getAttacked(monster, guess, AttackType.MAGIC);
			model.addAttackResult(attackResult);
			feedGuessRecord(playerSpirit, attackResult.getGuessRecord());
		}
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 200;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.MAGIC;
	}

	@Override
	public String getAttackName() {
		return "Explosion";
	}

}
