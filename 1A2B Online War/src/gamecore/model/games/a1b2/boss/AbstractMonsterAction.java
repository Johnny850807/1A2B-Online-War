package gamecore.model.games.a1b2.boss;

import java.util.List;

import utils.RandomString;

public abstract class AbstractMonsterAction implements MonsterAction{
	
	
	protected PlayerSpirit getRandomTargetPlayer(Boss1A2BGame game){
		List<PlayerSpirit> playerSpirits = game.getPlayerSpirits();
		return playerSpirits.get(random.nextInt(playerSpirits.size()));
	}
	
	protected String getGuess(){
		return RandomString.nextNonDuplicatedNumber(4);
	}
}
