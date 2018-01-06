package gamecore.model.games.a1b2.boss.core;

import java.util.Random;

import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;

public interface MonsterAction {
	Random random = new Random();
	
	/**
	 * to damage the player or do anything you want in the boss turn!!
	 */
	void execute(Monster monster, IBoss1A2BGame game);
	
	int getCostMp();
	
	AttackType getAttackType();
	
	String getAttackName();
}
