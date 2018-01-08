package gamecore.model.games.a1b2.boss.core;

import java.io.Serializable;

import org.omg.CORBA.PRIVATE_MEMBER;

import gamecore.model.ClientPlayer;
import gamecore.model.games.a1b2.core.GuessRecord;
import gamecore.model.games.a1b2.core.GuessResult;

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
	public int getDamage() {
		return damage;
	}
	public GuessRecord getGuessRecord() {
		return guessRecord;
	}
	public AbstractSpirit getAttacker() {
		return attacker;
	}
	public AbstractSpirit getAttacked() {
		return attacked;
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
