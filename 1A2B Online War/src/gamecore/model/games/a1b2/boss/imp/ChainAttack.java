package gamecore.model.games.a1b2.boss.imp;

import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;

import java.util.ArrayList;
import java.util.List;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;

public class ChainAttack extends AbstractSmartAttackAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		PlayerSpirit targetPlayer = getRandomTargetPlayer(game);
		List<AttackResult> attackResults = new ArrayList<>();
		for (int i = 0 ; i < 3 ; i ++)
		{
			String guess = produceSmartGuess(targetPlayer);
			AttackResult attackResult = targetPlayer.getAttacked(monster, guess, AttackType.MAGIC);
			attackResults.add(attackResult);
			feedGuessRecord(targetPlayer, attackResult.getGuessRecord());
		}
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster);
		model.setAttackResults(attackResults);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 200;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.MAGIC;
	}

	@Override
	public String getAttackName() {
		return "Chain Attack";
	}

}
