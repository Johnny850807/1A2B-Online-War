package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import utils.RandomString;

/**
 * @author Waterball
 * The boss who defeats you by only one punch.
 */
public class OnePunchBoss extends Monster{

	public OnePunchBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super("One Punch", Integer.MAX_VALUE, 2000, log, protocolFactory);
	}

	@Override
	protected List<MonsterAction> onCreateMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new PowerfulPunch());
		return actions;
	}
}
