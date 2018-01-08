package gamecore.model.games.a1b2.boss.core;

import java.util.Random;

import com.google.gson.Gson;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.core.A1B2NumberValidator;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.GuessResult;
import utils.ForServer;
import utils.MyGson;

public abstract class AbstractSpirit implements Spirit{
	protected transient ProtocolFactory protocolFactory;
	protected transient MyLogger log;
	protected transient static final Gson gson = MyGson.getGson();
	protected int hp;
	protected int mp;
	protected int maxHp;
	protected String answer;
	protected String name;
	protected String id;
	protected Type type;
	
	public AbstractSpirit(String id, String name, int maxHp, int mp, MyLogger log, ProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
		this.log = log;
		this.id = id;
		this.name = name;
		this.type = getType();
		this.hp = maxHp;
		this.maxHp = maxHp;
		this.mp = mp;
	}

	public int getMaxHp() {
		return maxHp;
	}
	public int getMp() {
		return mp;
	}	
	public int getHp() {
		return hp;
	}
	
	public void setMaxHp(int maxHp) {
		this.hp = this.maxHp = maxHp;
	}
	
	public void setMp(int mp) {
		this.mp = mp;
	}
	
	public abstract Type getType();
	public interface DamageParser{
		int parsingDamage(GuessResult guessResult);
	}

	protected transient static final DamageParser defaultDamageParser = new DamageParser(){
		@Override
		public int parsingDamage(GuessResult guessResult) {
			// formula (r = random number): (28+r)*(a+b) - ((7+r)*b) 
			int a = guessResult.getA(), b = guessResult.getB();
			return (28+getRandom(5, 13))*(a+b) - (7+getRandom(1, 6))*b + getRandom(1, 15) + (a>=3?70:0) + (a==4?100:0);
		}
	};
	
	public static final DamageParser getDefaultdamageparser() {
		return defaultDamageParser;
	}
	
	@Override
	@ForServer
	public AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType) {
		return getAttacked(attacker, guess, attackType, defaultDamageParser);
	}
	
	@Override
	@ForServer
	public AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType, DamageParser damageParser) {
		log.trace(getName() + " gets attacked by " + attacker.getName() + " with guessing: " + guess);
		GuessResult guessResult = A1B2NumberValidator.getGuessResult(answer, guess);
		log.trace("result: " + guessResult);
		int damage = damageParser.parsingDamage(guessResult);
		log.trace("damage:" + damage);
		GuessRecord guessRecord = new GuessRecord(guess, guessResult);
		AttackResult attackResult =  new AttackResult(damage, attackType, guessRecord, attacker, this);
		onDamaging(attackResult);
		return attackResult;
	}

	protected void onDamaging(AttackResult attackResult){
		costHp(attackResult.getDamage());
		if (isDead())
			onDie(attackResult);
		else 
		{
			if (attackResult.getA() == 4)
				onAnswerGuessed4A(attackResult);
			onSurvivedFromAttack(attackResult);
		}
	}
	
	private static int getRandom(int min, int max){
		return new Random().nextInt(max+1) + min;
	}
	
	public void costHp(int damage){
		hp = hp - damage < 0 ? 0 : hp - damage;
	}
	
	public void costMp(int cost){
		mp = mp - cost < 0 ? 0 : mp - cost;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
		log.trace("The boss has set up the answer: " + answer);
	}
	
	public boolean isDead(){
		return getHp() <= 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSpirit other = (AbstractSpirit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("'%s, hp: %d/%d, mp:%d, %s'", getName(), getHp(), getMaxHp(), getMp(), 
								isDead() ? "dead" : "alive");
	}
	
	public ProtocolFactory getProtocolFactory() {
		return protocolFactory;
	}
}
