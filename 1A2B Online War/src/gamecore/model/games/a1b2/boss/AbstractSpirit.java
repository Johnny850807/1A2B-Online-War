package gamecore.model.games.a1b2.boss;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.GuessRecord;
import gamecore.model.games.a1b2.GuessResult;
import utils.ForServer;

public abstract class AbstractSpirit implements Spirit{
	protected transient ProtocolFactory protocolFactory;
	protected transient MyLogger log;
	protected int hp;
	protected int mp;
	protected int maxHp;
	protected String answer;
	protected String name;
	protected String id;
	protected Type type;
	
	public AbstractSpirit(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
		this.log = log;
		this.id = id;
		this.name = name;
		this.type = getType();
		this.hp = getMaxHp();
		this.maxHp = getMaxHp();
		this.mp = getMp();
	}

	public abstract Type getType();
	public abstract int getMp();
	public abstract int getMaxHp();
	
	@Override
	@ForServer
	public AttackResult damage(AbstractSpirit attacker, String guess) {
		GuessResult guessResult = A1B2NumberValidator.getGuessResult(answer, guess);
		int damage = onParsingDamage(guessResult);
		GuessRecord guessRecord = new GuessRecord(guess, guessResult);
		AttackResult attackResult =  new AttackResult(damage, guessRecord, attacker, this);
		onDamaging(attackResult);
		return attackResult;
	}

	protected int onParsingDamage(GuessResult guessResult){
		return /*TODO*/ 100;
	}
	
	protected void onDamaging(AttackResult attackResult){
		hp = hp - attackResult.getDamage() < 0 ? 0 : hp - attackResult.getDamage();
		if (hp == 0)
			onDie(attackResult);
		else if (attackResult.getA() == 4)
			onAnswerGuessed4A(attackResult);
		else
			onSurvivedFromAttack(attackResult);
	}
	
	/**
	 * when the answer has been guessed correctly with the 4A result.
	 */
	protected abstract void onAnswerGuessed4A(AttackResult attackResult);
	
	/**
	 * when spirit got damaged and the hp became to 0.
	 */
	protected abstract void onDie(AttackResult attackResult);
	
	protected abstract void onSurvivedFromAttack(AttackResult attackResult);
	
	public enum Type{
		MONSTER, PLAYER
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
