package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.List;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;

/**
 * @author Waterball
 * the boss for testing with a silly answer.
 */
public class TestingBoss extends Monster{
	public static final String ANSWER = "1234";
	public TestingBoss(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
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
