package gamecore.model.games.a1b2.boss.imp;

import gamecore.model.games.a1b2.boss.core.AttackActionModel;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.GuessResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;

/**
 * @author Waterball
 */
public class SelfCuring implements MonsterAction {
	@Override
	public void execute(Monster monster, IBoss1A2BGame game) {
		GuessRecord mockRecord = new GuessRecord("Curing", new GuessResult(0, 0));
		int cureNumber = monster.getMaxHp() / 5;
		monster.costHp(cureNumber * -1);
		AttackResult curing = new AttackResult((cureNumber*-1), getAttackType(), mockRecord, monster, monster);
		AttackActionModel model = new AttackActionModel(getAttackName(), getCostMp(), monster, curing);
		game.addAllResultsAndbroadcastAttackActionModel(model);
	}

	@Override
	public int getCostMp() {
		return 150;
	}


	@Override
	public AttackType getAttackType() {
		return AttackType.ASSISTIVE;
	}

	@Override
	public String getAttackName() {
		return "Self Curing";
	}

}
