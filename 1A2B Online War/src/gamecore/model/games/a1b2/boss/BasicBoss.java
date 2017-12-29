package gamecore.model.games.a1b2.boss;

import java.util.UUID;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;

public class BasicBoss extends Monster{
	protected Boss1A2BGame context;
	
	public BasicBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super(UUID.randomUUID().toString(), "Boss", log, protocolFactory);
	}

	@Override
	public void init(Boss1A2BGame context) {
		this.context = context;
	}
	
	
	@Override
	public void setAnswer(String answer) {
		
	}

	@Override
	public void action() {
		
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
