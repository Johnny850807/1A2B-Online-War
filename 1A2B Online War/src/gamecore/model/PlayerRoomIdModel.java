package gamecore.model;

public class PlayerRoomIdModel {
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
