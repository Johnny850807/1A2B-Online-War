package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.List;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import utils.RandomString;

/**
 * @author Waterball
 * The boss who defeats you by only one punch.
 */
public class OnePunchBoss extends Monster{

	public OnePunchBoss(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}

	@Override
	protected List<MonsterAction> createMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new PowerfulPunch());
		return actions;
	}

	@Override
	protected String produceAnswer() {
		return RandomString.nextNonDuplicatedNumber(4);
	}

	@Override
	public Type getType() {
		return Type.MONSTER;
	}

	@Override
	public int getMp() {
		return 0;
	}

	@Override
	public int getMaxHp() {
		return Integer.MAX_VALUE;
	}

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {}

	@Override
	protected void onDie(AttackResult attackResult) {}

	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {}

}
