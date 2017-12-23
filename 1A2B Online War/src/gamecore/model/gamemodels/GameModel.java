package gamecore.model.gamemodels;

import java.util.Date;

public abstract class GameModel {
	private Date launchDate = new Date();
	
	public GameModel() {}

 	public GameModel(Date luanchDate) {
 		this.launchDate = luanchDate;
	}
 	
}
