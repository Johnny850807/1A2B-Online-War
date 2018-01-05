package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.Monster;

/**
 * @author Waterball
 * the boss for testing with a silly answer.
 */
public class TestingBoss extends Monster{
	public static final String ANSWER = "1234";
	public TestingBoss(String id, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, "Test", log, protocolFactory);
	}

	@Override
	protected String onProduceAnswer() {
		return ANSWER;
	}
	
	@Override
	public int getMp() {
		return 200000;
	}

	@Override
	public int getMaxHp() {
		return 3000;
	}

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		setAnswer(onProduceAnswer());
		log.trace(getName() + " got guessed correctly.");
	}

	@Override
	protected void onDie(AttackResult attackResult) {
		log.trace(getName() + " dies.");
	}

}
