package gamecore.model.games.a1b2.boss;

import java.io.Serializable;

import utils.ForServer;

public interface Spirit extends Serializable{
	@ForServer
	AttackResult damage(AbstractSpirit attacker, String guess);
	
	@ForServer
	void setAnswer(String answer);
	
	@ForServer
	void onHisTurn();
}
