package gamecore.model.games.a1b2.boss.base;

import java.io.Serializable;

public class NextTurnModel implements Serializable{
	private AbstractSpirit whosTurn;
	
	public NextTurnModel(AbstractSpirit whosTurn){
		this.whosTurn = whosTurn;
	}
	
	public AbstractSpirit getWhosTurn() {
		return whosTurn;
	}
	
	public String getWhosTurnId(){
		return whosTurn.getId();
	}
}
