package gamecore.model.games.a1b2.boss.imp;

import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import utils.RandomString;

public class ChangeAnswer implements MonsterAction{

	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		monster.setAnswer(RandomString.nextNonDuplicatedNumber(4));
	}

	@Override
	public int getCostMp() {
		return 100;
	}

	@Override
	public AttackType getAttackType() {
		return AttackType.ASSISTIVE;
	}

	@Override
	public String getAttackName() {
		return "Change answer";
	}

}
