package gamecore.model.games.a1b2.boss.imp;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.core.GuessResult;

/**
 * @author ShuYong
 */
public class HurtMyselfAttackAction extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceRandomGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC, getHurtMyselfDamageParser(monster));
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster);
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
	public AttackType getAttackType() {
		return AttackType.MAGIC;
	}

	@Override
	public String getAttackName() {
		return "Hurting Punch";
	}
}
