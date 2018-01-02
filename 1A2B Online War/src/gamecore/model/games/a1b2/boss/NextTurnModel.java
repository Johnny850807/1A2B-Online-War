package gamecore.model.games.a1b2.boss;

import java.io.Serializable;

public class NextTurnModel implements Serializable{
	private AbstractSpirit whosTurn;
	
	public NextTurnModel(AbstractSpirit whosTurn){
		this.whosTurn = whosTurn;
	}
}
