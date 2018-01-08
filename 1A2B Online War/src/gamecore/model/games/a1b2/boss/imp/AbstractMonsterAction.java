package gamecore.model.games.a1b2.boss.imp;

import java.util.List;

import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import utils.RandomString;

public abstract class AbstractMonsterAction implements MonsterAction{
	
	protected PlayerSpirit getRandomTargetPlayer(IBoss1A2BGame game){
		List<PlayerSpirit> playerSpirits = game.getAlivePlayerSpirits();
		return playerSpirits.get(random.nextInt(playerSpirits.size()));
	}
	
	protected String produceRandomGuess(){
		return RandomString.nextNonDuplicatedNumber(4);
	}
}
