package gamecore.model.games.a1b2.boss;

import java.util.UUID;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;

public class BasicBoss extends Monster{

	public BasicBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super(UUID.randomUUID().toString(), "Boss", log, protocolFactory);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void setAnswer(String answer) {
		
	}

	@Override
	public void onHisTurn() {
		
	}


	@Override
	public Type getType() {
		return Type.MONSTER;
	}

	@Override
	public int getMp() {
		return 1000;
	}

	@Override
	public int getMaxHp() {
		return 3000;
	}

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		
	}

	@Override
	protected void onDie(AttackResult attackResult) {
		
	}

	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {
		
	}

}
