package gamecore.model.games.a1b2.boss;

import java.util.List;
import java.util.Random;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;

public abstract class Monster extends AbstractSpirit{
	protected transient static Random random = new Random();
	protected transient List<MonsterAction> actions;
	protected transient Boss1A2BGame game;
	
	public Monster(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}
	
	public void init(Boss1A2BGame game){
		this.actions = createMonsterActions();
		this.game = game;
		this.setAnswer(produceAnswer());
	}

	protected abstract List<MonsterAction> createMonsterActions();
	protected abstract String produceAnswer();

	public void action() {
		log.trace("Boss' turn, the boss is choosing his action.");
		MonsterAction action = chooseNextMonsterAction();
		if (action.getCostMp() > getMp())
			throw new IllegalStateException("The boss does not have enough mp to execute the action, please override the chooseNextMonsterActiob() method to write your own desicion method.");
		log.trace("the action chosen: " + action.getAttackName());
		costMp(action.getCostMp());
		action.execute(this, game);
	}
	
	/**
	 * how the boss choose his action in each turn.
	 */
	protected MonsterAction chooseNextMonsterAction(){
		return actions.get(random.nextInt(actions.size()));
	}
}
