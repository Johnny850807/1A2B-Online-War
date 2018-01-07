package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;

public class SmartBabyBoss extends Monster{

	public SmartBabyBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super("Baby", 3000, 1000, log, protocolFactory);
	}
	
	@Override
	protected List<MonsterAction> onCreateMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new SmartGuessingAttack());
		actions.add(new ChainAttack());
		return actions;
	}
	
	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		setAnswer(onProduceAnswer());
	}
}
