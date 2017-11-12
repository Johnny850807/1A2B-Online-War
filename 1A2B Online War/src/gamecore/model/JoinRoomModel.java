package gamecore.model;

public class JoinRoomModel {
	private String playerId;
	private String gameRoomId;
	
	public JoinRoomModel(String playerId, String gameRoomId) {
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
