package gamecore.model.games.a1b2.boss.imp;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;

public class MagicAttackAction extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceRandomGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC);
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster, attackResult);
		monster.costMp(getCostMp());
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 100;
	}
	
	@Override
	public AttackType getAttackType() {
		return AttackType.MAGIC;
	}
	

	@Override
	public String getAttackName() {
		return "Magic attack";  //TODO
	}
}
