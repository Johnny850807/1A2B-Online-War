package gamecore.model.games.a1b2.boss;

import java.io.Serializable;

import gamecore.model.games.a1b2.boss.AttackResult.AttackType;
import utils.ForServer;

public interface Spirit extends Serializable{
	@ForServer
	AttackResult getAttacked(AbstractSpirit attacker, String guess, AttackType attackType);
	
	@ForServer
	void setAnswer(String answer);
	
	@ForServer
	void action();
}
