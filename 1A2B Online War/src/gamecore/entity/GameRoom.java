package gamecore.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	private List<PlayerStatus> playerStatusList =  Collections.checkedList(new ArrayList<>(), PlayerStatus.class);
	private String name;
	
	public GameRoom(GameMode gameMode, String name, Player host) {
		this.gameMode = gameMode;
		this.name = name;
		this.host = host;
	}
	
	public void addChatMessage(ChatMessage chatMessage){
		assert chatMessage.getId() != null : "ChatMessage's id should be initialized.";
		chatMessageList.add(chatMessage);
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

	public List<PlayerStatus> getPlayerStatus() {
		return playerStatusList;
	}
	
	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		for (PlayerStatus status : playerStatusList)
			players.add(status.getPlayer());
		return players;
	}
	
	public void sendMessage(ChatMessage chatMessage){
		chatMessageList.add(chatMessage);
	}

	public void addPlayer(Player player){
		PlayerStatus playerStatus = new PlayerStatus(player);
		if (playerStatusList.contains(playerStatus))
			throw new IllegalStateException("Duplicated player added into the status list.");
		playerStatusList.add(playerStatus);
	}
	
	public void removePlayer(Player player){
		if (playerStatusList.contains(player))
			playerStatusList.remove(player);
		else 
			throw new IllegalArgumentException("The removed player doesn't exist in the room !");
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
	
	@Override
	public String toString() {
		return String.format("Room id: %s, name: %s, GameMode: %s, Host: %s, Players: %d/%d, Status: %s", 
				id, name, gameMode.toString(), host.getName(), getPlayers().size(), gameMode.getMaxPlayerAmount(), roomStatus.toString());
	}
}
