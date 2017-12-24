package gamecore.model.games.a1b2;

import gamecore.entity.Player;
import gamecore.model.GameMode;

public class GameOverModel {
	private String winnerId;
	private String roomId;
	private GameMode gameMode;
	private long gameDuration;
	
	public GameOverModel(String winnerId, String roomId, GameMode gameMode, long gameDuration) {
		this.winnerId = winnerId;
		this.roomId = roomId;
		this.gameMode = gameMode;
		this.gameDuration = gameDuration;
	}

	public String getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(String winnerId) {
		this.winnerId = winnerId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public long getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(long gameDuration) {
		this.gameDuration = gameDuration;
	}
	
	
}
