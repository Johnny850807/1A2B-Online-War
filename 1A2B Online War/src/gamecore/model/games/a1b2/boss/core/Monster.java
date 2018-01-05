package gamecore.model.games.a1b2.boss.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.imp.Boss1A2BGame;
import gamecore.model.games.a1b2.boss.imp.RandomGuessAttack;
import utils.RandomString;

public class Monster extends AbstractSpirit{
	protected transient static Random random = new Random();
	protected transient List<MonsterAction> actions;
	protected transient Boss1A2BGame game;
	
	public Monster(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}
	
	public void init(Boss1A2BGame game){
		this.actions = onCreateMonsterActions();
		this.game = game;
		this.setAnswer(onProduceAnswer());
	}

	protected List<MonsterAction> onCreateMonsterActions(){
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new RandomGuessAttack());
		return actions;
	}
	
	protected String onProduceAnswer() {
		return RandomString.nextNonDuplicatedNumber(4);
	}

	public void action() {
		log.trace("Boss' turn, the boss is choosing his action.");
		MonsterAction action = chooseNextMonsterAction();
		if (action.getCostMp() > getMp())
			throw new IllegalStateException("The boss does not have enough mp to execute the action, please override the chooseNextMonsterActiob() method to write your own desicion method.");
		log.trace("the action chosen: " + action.getAttackType());
		costMp(action.getCostMp());
		action.execute(this, game);
	}
	
	/**
	 * how the boss choose his action in each turn.
	 */
	protected MonsterAction chooseNextMonsterAction(){
		return actions.get(random.nextInt(actions.size()));
	}

	@Override
	public Type getType() {
		return Type.MONSTER;
	}

	@Override
	public int getMp() {
		return 200;
	}

	@Override
	public int getMaxHp() {
		return 500;
	}

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {/*hook*/}

	@Override
	protected void onDie(AttackResult attackResult) {/*hook*/}

	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {/*hook*/}
}
