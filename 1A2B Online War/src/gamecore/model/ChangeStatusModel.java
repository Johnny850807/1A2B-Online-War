package gamecore.model;

import java.io.Serializable;

public class ChangeStatusModel implements Serializable{
	private String playerId;
	private String roomId;
	private boolean prepare;
	
	public ChangeStatusModel(String playerId, String roomId, boolean prepare) {
		this.playerId = playerId;
		this.roomId = roomId;
		this.prepare = prepare;
	}

	public String getRoomId() {
		return roomId;
	}


	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}


	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isPrepare() {
		return prepare;
	}

	public void setPrepare(boolean prepare) {
		this.prepare = prepare;
	}
	
	
}
