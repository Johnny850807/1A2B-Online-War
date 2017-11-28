package gamecore.rooms.games;

import gamecore.entity.GameRoom;
import gamecore.model.GameMode;
import gamecore.model.IGameMode;
import gamecore.model.gamemodels.GameModel;

public abstract class Game implements IGameMode{
	private GameModel gameModel;
	private GameRoom gameRoom;
	private GameMode gameMode;
	
	public Game(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}

	public void setGameRoom(GameRoom gameRoom) {
		assert this.gameRoom == null : "Game's game room should be set only once time.";
		this.gameRoom = gameRoom;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public String getGameId(){
		return gameRoom.getId();
	}

	@Override
	public int getMinPlayerAmount() {
		return gameMode.getMinPlayerAmount();
	}
	
	@Override
	public int getMaxPlayerAmount() {
		return gameMode.getMaxPlayerAmount();
	}
	
}
