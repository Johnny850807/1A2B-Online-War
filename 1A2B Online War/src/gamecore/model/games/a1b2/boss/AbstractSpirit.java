package gamecore.model.games.a1b2.boss;

import java.util.Random;

import com.google.gson.Gson;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.A1B2NumberValidator;
import gamecore.model.games.a1b2.GuessRecord;
import gamecore.model.games.a1b2.GuessResult;
import gamecore.model.games.a1b2.boss.AttackResult.AttackType;
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
	
	public interface DamageParser{
		int parsingDamage(GuessResult guessResult);
	}

	protected transient static final DamageParser defaultDamageParser = new DamageParser(){
		@Override
		public int parsingDamage(GuessResult guessResult) {
			// formula (r = random number): (28+r)*(a+b) - ((7+r)*b) 
			int a = guessResult.getA(), b = guessResult.getB();
			return (28+getRandom(5, 13))*(a+b) - (7+getRandom(1, 6))*b + getRandom(1, 15) + (a==4?80:0);
		}
	};
	
	@Override
	@ForServer
	public AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType) {
		return getAttacked(attacker, guess, attackType, defaultDamageParser);
	}
	
	@Override
	@ForServer
	public AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType, DamageParser damageParser) {
		GuessResult guessResult = A1B2NumberValidator.getGuessResult(answer, guess);
		int damage = damageParser.parsingDamage(guessResult);
		GuessRecord guessRecord = new GuessRecord(guess, guessResult);
		AttackResult attackResult =  new AttackResult(damage, attackType, guessRecord, attacker, this);
		onDamaging(attackResult);
		return attackResult;
	}

	
	private static int getRandom(int min, int max){
		return new Random().nextInt(max+1) + min;
	}
	
	void costHp(int damage){
		hp = hp - damage < 0 ? 0 : hp - damage;
	}
	
	void costMp(int cost){
		mp = mp - cost < 0 ? 0 : mp - cost;
	}
	
	protected void onDamaging(AttackResult attackResult){
		costHp(attackResult.getDamage());
		if (isDead())
			onDie(attackResult);
		else if (attackResult.getA() == 4)
			onAnswerGuessed4A(attackResult);
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

	public int getHp() {
		return hp;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean isDead(){
		return getHp() <= 0;
	}
	
}
