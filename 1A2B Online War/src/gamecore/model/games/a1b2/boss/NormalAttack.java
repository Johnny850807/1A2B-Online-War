package gamecore.model.games.a1b2.boss;

import java.util.List;

import gamecore.model.games.a1b2.boss.AttackResult.AttackType;
import utils.RandomString;

/**
 * @author ShuYong
 */
public class NormalAttack implements MonsterAction{
	
	@Override
	public void execute(Monster monster, Boss1A2BGame game) {
		PlayerSpirit targetPlayer = getTargetPlayer(game);
		String guess = getGuess();
		AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.NORMAL);
		AttackActionModel model = new AttackActionModel(0, monster);
		model.addAttackResult(attackResult);
		game.broadcastAttackActionModel(model);
	}

	protected PlayerSpirit getTargetPlayer(Boss1A2BGame game){
		List<PlayerSpirit> playerSpirits = game.getPlayerSpirits();
		return playerSpirits.get(random.nextInt(playerSpirits.size()));
	}
	
	protected String getGuess(){
		return RandomString.nextNonDuplicatedNumber(4);
	}
}
