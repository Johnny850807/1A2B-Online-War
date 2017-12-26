package gamecore.model;

import java.io.Serializable;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;

public class PlayerRoomModel implements Serializable{
	private Player player;
	private GameRoom gameRoom;
	
	public PlayerRoomModel(Player player, GameRoom gameRoom) {
		this.player = player;
		this.gameRoom = gameRoom;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public GameRoom getGameRoom() {
		return gameRoom;
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
	}
	
	
}
