package gamecore.model.games.a1b2.boss;

import java.util.Random;

import gamecore.model.games.a1b2.boss.AttackResult.AttackName;

public interface MonsterAction {
	Random random = new Random();
	
	/**
	 * to damage the player or do anything you want in the boss turn!!
	 */
	void execute(Monster monster, Boss1A2BGame game);
	
	int getCostMp();
	
	AttackName getAttackName();
}
