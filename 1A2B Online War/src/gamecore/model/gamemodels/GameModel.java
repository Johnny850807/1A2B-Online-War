package gamecore.model.gamemodels;

import java.util.Date;

import gamecore.model.GameMode;

public abstract class GameModel {
	private GameMode gameMode;
	private Date launchDate = new Date();
	
	public GameModel(GameMode gameMode) {
		this.gameMode = gameMode;
	}

 	public GameModel(Date luanchDate, GameMode gameMode) {
 		this(gameMode);
 		this.launchDate = luanchDate;
	}
 	
}
