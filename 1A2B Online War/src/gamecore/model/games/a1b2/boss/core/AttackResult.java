package gamecore.model.games.a1b2.boss.core;

import java.io.Serializable;

import org.omg.CORBA.PRIVATE_MEMBER;

import gamecore.model.ClientPlayer;
import gamecore.model.games.a1b2.duel.core.GuessRecord;
import gamecore.model.games.a1b2.duel.core.GuessResult;

public class AttackResult implements Serializable{
	private int damage;
	private AttackType attackType;
	private GuessRecord guessRecord;
	private AbstractSpirit attacker;
	private AbstractSpirit attacked;
	
	public AttackResult(int damage, AttackType attackType, GuessRecord guessRecord, AbstractSpirit attacker, AbstractSpirit attacked) {
		this.damage = damage;
		this.attackType = attackType;
		this.guessRecord = guessRecord;
		this.attacker = attacker;
		this.attacked = attacked;
	}
	
	public AttackType getAttackType() {
		return attackType;
	}
	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public GuessRecord getGuessRecord() {
		return guessRecord;
	}
	public void setGuessRecord(GuessRecord guessRecord) {
		this.guessRecord = guessRecord;
	}
	public AbstractSpirit getAttacker() {
		return attacker;
	}
	public void setAttacker(AbstractSpirit attacker) {
		this.attacker = attacker;
	}
	public AbstractSpirit getAttacked() {
		return attacked;
	}
	public void setAttacked(AbstractSpirit attacked) {
		this.attacked = attacked;
	}
	
	public int getA(){
		return this.getGuessRecord().getA();
	}
	
	public int getB(){
		return this.getGuessRecord().getB();
	}
	
	/**
	 * The attack name, it's important for the client-side detecting how to render the attacking effect
	 */
	public enum AttackType{
		NORMAL, MAGIC, ASSISTIVE
	}
}
