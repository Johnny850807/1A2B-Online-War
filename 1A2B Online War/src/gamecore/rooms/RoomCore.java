package gamecore.rooms;

import java.util.ArrayList;
import java.util.List;

import gamecore.entity.Entity;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.RoomStatus;
import gamecore.rooms.games.Game;

public abstract class RoomCore{
	private GameRoom roomInfo;
	private List<Player> users = new ArrayList<>();
	private RoomStatus roomStatus;
	private Game game;
	private Player host;
	private String name;

	
	public RoomCore(String name) {
		this.name = name;
	}
	
	public void launchGame(){
		if (roomStatus == RoomStatus.gamestarted || game != null)
			throw new IllegalStateException("The room has been launched the game.");
		roomStatus = RoomStatus.gamestarted;
		this.game = createGame();
		
		//TODO launch the game logic
	}
	
	public abstract Game createGame();

	public String getId(){
		return roomInfo.getId();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Player> getUsers() {
		return users;
	}

	public void setUsers(List<Player> users) {
		this.users = users;
	}

	public RoomStatus getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(RoomStatus roomStatus) {
		this.roomStatus = roomStatus;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Player getHost() {
		return host;
	}

	public void setHost(Player host) {
		this.host = host;
	}
	
	
}
