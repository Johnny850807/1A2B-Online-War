package gamecore.model;

/**
 * @author Waterball
 * the model contains the player's id, room's id and the certain content.
 */
public class ContentModel {
	private String playerId;
	private String roomId;
	private String content;
	
	public ContentModel(String playerId, String roomId, String content) {
		this.roomId = roomId;
		this.playerId = playerId;
		this.content = content;
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

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
