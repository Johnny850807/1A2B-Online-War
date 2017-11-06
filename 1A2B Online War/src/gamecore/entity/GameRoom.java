package gamecore.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Linq.Linq;
import gamecore.model.GameMode;
import gamecore.model.PlayerStatus;
import gamecore.model.RoomStatus;

/**
 * GameRoom contains only the info and the status the room should present. The game of the room will be
 * created by a GameMode.
 */
public class GameRoom extends Entity{
	private Player host;
	private RoomStatus roomStatus = RoomStatus.waiting;
	private GameMode gameMode;
	private List<ChatMessage> chatMessageList = Collections.checkedList(new ArrayList<>(), ChatMessage.class);
	private List<PlayerStatus> playerstatusList = Collections.checkedList(new ArrayList<>(), PlayerStatus.class);
	private String name;
	
	public GameRoom(GameMode gameMode, String name, Player host) {
		this.gameMode = gameMode;
		this.name = name;
		this.host = host;
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public String getName() {
		return name;
	}

	public Player getHost() {
		return host;
	}

	public void setHost(Player host) {
		this.host = host;
	}

	public RoomStatus getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(RoomStatus roomStatus) {
		this.roomStatus = roomStatus;
	}

	public List<PlayerStatus> getPlayers() {
		return playerstatusList;
	}
	
	public void sendMessage(ChatMessage chatMessage){
		chatMessageList.add(chatMessage);
	}

	public void addPlayer(Player player){
		if (playerstatusList.contains(player))
			throw new IllegalStateException("Duplicated player added into the status list.");
		playerstatusList.add(new PlayerStatus(player));
	}
	
	public void removePlayer(Player player){
		for (PlayerStatus status : playerstatusList)
			if (status.getPlayer().equals(player))
			{
				playerstatusList.remove(status);
				break;
			}
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaxPlayerAmount(){
		return gameMode.getMaxPlayerAmount();
	}
	
	public int getMinPlayerAmount(){
		return gameMode.getMinPlayerAmount();
	}
}
