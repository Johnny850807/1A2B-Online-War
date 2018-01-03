package gamecore.model.games.a1b2.boss;

import gamecore.entity.Player;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.boss.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.AttackResult.AttackName;

/**
 * @author Waterball
 * One punch to defeat you.
 */
public class PowerfulPunch extends AbstractMonsterAction{

	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		String guess = produceGuess();
		DamageParser powerfulDamaging = powerfulDamaging(targetPlayer);
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, getAttackName(), powerfulDamaging);
		AttackActionModel model = new AttackActionModel(getCostMp(), monster, attackResult);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	public DamageParser powerfulDamaging(PlayerSpirit targetPlayer){
		return new DamageParser() {
			@Override
			public int parsingDamage(GuessResult guessResult) {
				int rand = random.nextInt(3)+1;
				return targetPlayer.getMaxHp() / rand;
			}
		};
	}
	
	@Override
	public int getCostMp() {
		return 0;
	}

	@Override
	public AttackName getAttackName() {
		return AttackName.NORMAL;
	}

}
