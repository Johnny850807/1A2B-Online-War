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
			setMaxHp(2000);
			setMp(800);
			break;
		case 3:
			setMaxHp(5500);
			setMp(1500);
			break;
		case 4:
			setMaxHp(8200);
			setMp(2900);
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
		return actions;
	}
	
	@Override
	protected boolean filterAction(MonsterAction monsterAction) {
		//always use magic when the hp is left half and the mp is enough, .
		if (monsterAction.getAttackType() == AttackType.NORMAL)  
			return getHp() > getMaxHp() / 2 && getMp() <= chainAttack.getCostMp(); 
			
		// the magic can only be used when the hp is left half.
		if (monsterAction.getAttackType() == AttackType.MAGIC) 
			return getHp()  <= getMaxHp() / 2;  
		
		if (monsterAction instanceof ChangeAnswer)
			return changingAnswerDegree >= 100;
		return true;
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
		// change the answer if the degree is greater than 100
		if (changingAnswerDegree >= 100 && getMp() >= changeAnswer.getCostMp())
			return changeAnswer;
		return super.chooseNextMonsterAction();
	}
	
	
	@Override
	public void setAnswer(String answer) {
		super.setAnswer(answer);
		changingAnswerDegree = 0;  //remove the state whenever the answer changed
	}
}
