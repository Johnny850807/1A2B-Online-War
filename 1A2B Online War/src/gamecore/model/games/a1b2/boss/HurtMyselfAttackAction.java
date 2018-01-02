package gamecore.model.games.a1b2.boss;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.boss.AbstractSpirit.DamageParser;

import gamecore.model.games.a1b2.boss.AttackResult.AttackType;

public class HurtMyselfAttackAction extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getTargetPlayer(game);
		String guess = getGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC,getHurtMyselfDamageParser(monster));
		AttackActionModel model = new AttackActionModel(getCostMp(), monster);
		model.addAttackResult(attackResult);
		game.broadcastAttackActionModel(model);
		
	}

	@Override
	public int getCostMp() {
		return 200;
	}

	public DamageParser getHurtMyselfDamageParser(Monster monster){
		return new DamageParser() {
			
			@Override
			public int parsingDamage(GuessResult guessResult) {
			
				int OneThirdOfHp =monster.getHp() / 3;
				monster.costHp(OneThirdOfHp);
				return (monster.getHp() / 50)+100;
			}
		};
			
	}

}
