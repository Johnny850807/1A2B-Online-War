package gamecore.model.games.a1b2.boss;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.boss.AbstractSpirit.DamageParser;

import gamecore.model.games.a1b2.boss.AttackResult.AttackName;

/**
 * @author ShuYong
 */
public class HurtMyselfAttackAction extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackName.MAGIC, getHurtMyselfDamageParser(monster));
		AttackActionModel model = new AttackActionModel(getCostMp(), monster);
		model.addAttackResult(attackResult);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 200;
	}

	public DamageParser getHurtMyselfDamageParser(Monster monster) {
		return new DamageParser() {
			@Override
			public int parsingDamage(GuessResult guessResult) {
				int OneThirdOfHp =monster.getHp() / 3;
				monster.costHp(OneThirdOfHp);
				monster.costMp(getCostMp());
				return (monster.getHp() / 50)+100;
			}
		};
	}
	
	@Override
	public AttackName getAttackName() {
		return AttackName.MAGIC;
	}
}
