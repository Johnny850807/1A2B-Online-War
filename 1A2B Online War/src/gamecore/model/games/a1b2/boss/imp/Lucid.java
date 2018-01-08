package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;

public class Lucid extends Monster{
	private transient ChangeAnswer changeAnswer;
	private transient SmartGuessingAttack smartGuessingAttack;
	private transient ChainAttack chainAttack;
	private transient ExplosionAttack explosionAttack;
	private transient SelfCuring selfCuring;
	
	/**
	 * make the decision that if the boss has to change the answer
	 */
	private int changingAnswerDegree = 0; // change the answer if greater than 100
	
	public Lucid(MyLogger log, ProtocolFactory protocolFactory) {
		super("Lucid", 700, 400, log, protocolFactory);
	}
	
	@Override
	public void init(IBoss1A2BGame game) {
		super.init(game);
		int playerAmount = game.getPlayerSpirits().size();
		/**
		 * the hp/mp depends on the player amount:
		 */
		switch (playerAmount) {
		case 2:
			setMaxHp(2500);
			setMp(800);
			break;
		case 3:
			setMaxHp(5800);
			setMp(2100);
			break;
		case 4:
			setMaxHp(8700);
			setMp(3500);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected List<MonsterAction> onCreateMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(smartGuessingAttack = new SmartGuessingAttack());
		actions.add(chainAttack = new ChainAttack());
		actions.add(changeAnswer = new ChangeAnswer());
		actions.add(explosionAttack = new ExplosionAttack());
		actions.add(selfCuring = new SelfCuring());
		return actions;
	}
	

	@Override
	protected void onDamaging(AttackResult attackResult) {
		if (attackResult.getA() == 4)  //4A
			changingAnswerDegree += 100;
		else if (attackResult.getA() + attackResult.getB() == 4)  //4B, 2A2B, 3A1B, 1A3B...
			changingAnswerDegree += 33;
		else if (attackResult.getA() >= 3 || attackResult.getB() >= 3)  //3A, 3B
			changingAnswerDegree += 20;
		super.onDamaging(attackResult);
	}
	
	
	@Override
	protected MonsterAction chooseNextMonsterAction() {
		int playerAmount = game.getPlayerSpirits().size();
		
		// change the answer if the degree is greater than 100
		if (changingAnswerDegree >= 100 && getMp() >= changeAnswer.getCostMp())
			return changeAnswer;
		
		if (playerAmount >= 3)  //self curing when have 3 or 4 players
			if (getHp() <= getMaxHp() / 7 && getMp() >= selfCuring.getCostMp())
				return selfCuring;
		
		//using the magic attack only if the hp left half
		if (getHp() <= getMaxHp() / 2 && getMp() >= chainAttack.getCostMp())
		{
			if (random.nextInt(100) < 50 || playerAmount == 1) //chain only if only one player
				return chainAttack;
			else
				return explosionAttack;
		}
		return smartGuessingAttack;
	}
	
	
	@Override
	public void setAnswer(String answer) {
		super.setAnswer(answer);
		changingAnswerDegree = 0;  //remove the state whenever the answer changed
	}
}
