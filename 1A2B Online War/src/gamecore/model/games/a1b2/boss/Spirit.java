package gamecore.model.games.a1b2.boss;

import java.io.Serializable;

import gamecore.model.games.a1b2.boss.AbstractSpirit.DamageParser;
import gamecore.model.games.a1b2.boss.AttackResult.AttackName;
import utils.ForServer;

public interface Spirit extends Serializable{
	@ForServer
	AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackName attackType);
	@ForServer
	AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackName attackType, DamageParser damageParser);
	
	@ForServer
	void setAnswer(String answer);
}
