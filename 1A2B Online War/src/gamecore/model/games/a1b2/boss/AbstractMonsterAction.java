package gamecore.model.games.a1b2.boss;

import java.util.List;

import gamecore.model.games.a1b2.boss.AttackResult.AttackName;
import utils.RandomString;

public abstract class AbstractMonsterAction implements MonsterAction{
	
	
	protected PlayerSpirit getRandomTargetPlayer(Boss1A2BGame game){
		List<PlayerSpirit> playerSpirits = game.getAlivePlayerSpirits();
		return playerSpirits.get(random.nextInt(playerSpirits.size()));
	}
	
	protected String produceGuess(){
		return RandomString.nextNonDuplicatedNumber(4);
	}
}
