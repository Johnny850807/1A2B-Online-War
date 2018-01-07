package gamecore.model.games.a1b2.boss.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import container.core.Constants.Events.Games.Boss1A2B;
import container.core.MyLogger;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.RequestStatus;
import gamecore.model.games.a1b2.boss.imp.RandomGuessAttack;
import utils.ForServer;
import utils.RandomString;

public class Monster extends AbstractSpirit{
	protected transient static Random random = new Random();
	protected transient List<MonsterAction> actions;
	protected transient IBoss1A2BGame game;
	
	public Monster(String name, int maxHp, int mp, MyLogger log, ProtocolFactory protocolFactory) {
		super(UUID.randomUUID().toString(), name, maxHp, mp, log, protocolFactory);
	}
	
	public void init(IBoss1A2BGame game){
		this.actions = onCreateMonsterActions();
		this.game = game;
		this.setAnswer(onProduceAnswer());
	}

	/**
	 * only the random guess attack as default
	 */
	protected List<MonsterAction> onCreateMonsterActions(){
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new RandomGuessAttack());
		return actions;
	}
	
	protected String onProduceAnswer() {
		return RandomString.nextNonDuplicatedNumber(4);
	}

	@ForServer
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
		List<MonsterAction> mpAvailableActions = new ArrayList<>();
		for (MonsterAction monsterAction : actions)
			if (filterAction(monsterAction) && monsterAction.getCostMp() <= getMp())
				mpAvailableActions.add(monsterAction);
		return mpAvailableActions.get(random.nextInt(mpAvailableActions.size()));
	}
	/**
	 * @param monsterAction
	 * @return return true if the monster can choose this action randomly by default.
	 */
	protected boolean filterAction(MonsterAction monsterAction){
		return true;  //all actions are the candidate actions to be choose randomly by default.
	}
	
	@Override
	public Type getType() {
		return Type.MONSTER;
	}

	@Override
	public void setAnswer(String answer) {
		super.setAnswer(answer);
		Protocol protocol = protocolFactory.createProtocol(Boss1A2B.CHANGE_ANSWER, RequestStatus.success.toString(),
				gson.toJson(this));
		game.broadcastToAll(protocol);
	}
	
	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {/*hook*/}

	@Override
	protected void onDie(AttackResult attackResult) {/*hook*/}

	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {/*hook*/}
}
