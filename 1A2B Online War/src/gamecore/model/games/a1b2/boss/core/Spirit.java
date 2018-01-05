package gamecore.model.games.a1b2.boss.core;

import java.io.Serializable;

import gamecore.model.games.a1b2.boss.core.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import utils.ForServer;

public interface Spirit extends Serializable{
	@ForServer
	AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType);
	@ForServer
	AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType, DamageParser damageParser);
	
	@ForServer
	void setAnswer(String answer);
}
