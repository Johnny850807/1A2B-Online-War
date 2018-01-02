package gamecore.model.games.a1b2.boss;

import java.util.List;
import java.util.Random;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import utils.RandomString;

public abstract class Monster extends AbstractSpirit{
	protected List<MonsterAction> actions;
	protected Boss1A2BGame game;
	protected Random random = new Random();
	
	public Monster(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}
	
	public void init(Boss1A2BGame game){
		this.actions = createMonsterActions();
		this.game = game;
		this.setAnswer(RandomString.nextNonDuplicatedNumber(4));
	}

	protected abstract List<MonsterAction> createMonsterActions();
	
	@Override
	public void action() {
		MonsterAction action = getMonsterAction();
		action.execute(this, game);
	}
	
	protected MonsterAction getMonsterAction(){
		return actions.get(random.nextInt(actions.size()));
	}
}
