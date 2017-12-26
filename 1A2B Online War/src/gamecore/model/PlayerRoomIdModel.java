package gamecore.model;

import java.io.Serializable;

public class PlayerRoomIdModel implements Serializable{
	private String playerId;
	private String gameRoomId;
	
	public PlayerRoomIdModel(String playerId, String gameRoomId) {
		this.playerId = playerId;
		this.gameRoomId = gameRoomId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getGameRoomId() {
		return gameRoomId;
	}
	
}
