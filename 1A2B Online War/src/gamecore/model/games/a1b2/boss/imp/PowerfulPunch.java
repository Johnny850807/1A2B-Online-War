package gamecore.model.games.a1b2.boss.imp;

import gamecore.entity.Player;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit;
import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.duel.core.GuessResult;

/**
 * @author Waterball
 * One punch to defeat you.
 */
public class PowerfulPunch extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceRandomGuess();
		DamageParser powerfulDamaging = powerfulDamaging(targetPlayer);
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, getAttackType(), powerfulDamaging);
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster, attackResult);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	public DamageParser powerfulDamaging(PlayerSpirit targetPlayer){
		return new DamageParser() {
			@Override
			public int parsingDamage(GuessResult guessResult) {
				int damage = AbstractSpirit.getDefaultdamageparser().parsingDamage(guessResult);
				damage += guessResult.getA() * 20;
				return damage;
			}
		};
	}
	
	@Override
	public int getCostMp() {
		return 55;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.MAGIC;
	}
	
	@Override
	public String getAttackName() {
		return "Powerful punch";
	}
}
