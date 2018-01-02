package gamecore.model.games.a1b2.boss;

import gamecore.model.games.a1b2.boss.AttackResult.AttackType;

public class MagicAttackAction extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = getGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC);
		AttackActionModel model = new AttackActionModel(getCostMp(), monster, attackResult);
		monster.costMp(getCostMp());
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 100;
	}
}
