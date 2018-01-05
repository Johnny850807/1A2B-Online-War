package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;

public class SmartBabyBoss extends Monster{

	public SmartBabyBoss(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}
	
	@Override
	protected List<MonsterAction> onCreateMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new SmartGuessingAttack());
		return super.onCreateMonsterActions();
	}

}
