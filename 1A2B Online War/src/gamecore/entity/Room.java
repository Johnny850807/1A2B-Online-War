package gamecore.entity;

import java.util.ArrayList;
import java.util.List;

import gamecore.RoomStatus;

public abstract class Room extends Entity{
	private List<User> users = new ArrayList<>();
	private RoomStatus roomStatus;
	private Game game;
	private User host;
	private String name;

	
	public Room(String name) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
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

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}
	
	
}
