package gamecore.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Expose;

import container.ApacheLoggerAdapter;
import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.ClientBinder;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.GameMode;
import gamecore.model.MockLogger;
import gamecore.model.PlayerStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;
import gamecore.model.games.a1b2.Duel1A2BGame;
import utils.ClientPlayersHelper;

/**
 * GameRoom contains only the info and the status the room should present. The game of the room will be
 * created by a GameMode.
 */
public class GameRoom extends Entity{
	private transient ProtocolFactory protocolFactory;
	private transient MyLogger log = new MockLogger();
	private Player host;
	private RoomStatus roomStatus = RoomStatus.waiting;
	private GameMode gameMode;
	private Game game;
	private List<ChatMessage> chatMessageList = Collections.checkedList(new ArrayList<>(), ChatMessage.class);
	
	/**
	 * All the guest player status in the room, except the host.
	 */
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
	
	public int getChatMessagesSize(){
		return getChatMessageList().size();
	}
	
	public List<ChatMessage> getChatMessageList() {
		return chatMessageList;
	} 
	
	public void setLog(MyLogger log) {
		this.log = log;
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
	
	public int getPlayerAmount(){
		return getPlayers().size(); 
	}
	
	public void changePlayerStatus(Player player, boolean ready){
		getPlayerStatusOfPlayer(player).setReady(ready);
	}
	
	/**
	 * @return all the players including the host at the first position.
	 */
	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		players.add(host);
		for (PlayerStatus playerStatus : playerStatusList)
			players.add(playerStatus.getPlayer());
		return players;
	}

	public void addPlayer(Player player){
		player.setUserStatus(ClientStatus.inRoom);
		PlayerStatus playerStatus = new PlayerStatus(player);
		if (host.equals(player) || playerStatusList.contains(playerStatus))
			throw new IllegalStateException("Duplicated player added into the status list.");
		if (playerStatusList.size() > getMaxPlayerAmount())
			throw new IllegalStateException("The Player amount is out of the maximum amount.");
		playerStatusList.add(playerStatus);
	}
	
	public void removePlayer(Player player){
		playerStatusList.remove(getPlayerStatusOfPlayer(player));
	}
	
	public PlayerStatus getPlayerStatusOfPlayer(Player player){
		for (PlayerStatus playerStatus : playerStatusList)
			if(playerStatus.getPlayer().equals(player))
				return playerStatus;
		throw new IllegalStateException("The removed player doesn't exist in the room !");
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
	
	public boolean containsPlayer(Player player){
		return host.equals(player) || ifPlayerInStatusList(player);
	}
	
	public boolean ifPlayerInStatusList(Player player){
		try{
			getPlayerStatusOfPlayer(player);
			return true;
		}catch (IllegalArgumentException e) {
			//if the player is not in the list, IllegalStateException thrown.
			return false;
		}
	}
	
	public void setProtocolFactory(ProtocolFactory protocolFactory) {
		this.protocolFactory = protocolFactory;
	}
	
	public ProtocolFactory getProtocolFactory() {
		return protocolFactory;
	}
	
	/**
	 * launch the game and send all the client players into the game.
	 * @param clientBinder binding interface which allows the game access the client player without coupling to the game core.
	 */
	public void launchGame(ClientBinder clientBinder){
		log.trace("Room: " + id + ", launcing the " + gameMode.toString() + " game.");
		validatePlayerAmount();
		ClientPlayer hostClient = clientBinder.getClientPlayer(host.getId());
		log.trace("Host prepared: " + hostClient.getPlayerName());
		List<ClientPlayer> playerClients = new ArrayList<>();
		for(PlayerStatus playerStatus : playerStatusList)
			playerClients.add(clientBinder.getClientPlayer(playerStatus.getPlayer().getId()));
		log.trace("Host prepared: " + hostClient.getPlayerName());
		log.trace("Players prepared: " + ClientPlayersHelper.toString(playerClients));
		initGameAndStart(hostClient, playerClients);
	}
	
	private void validatePlayerAmount(){
		int playerAmount = getPlayerAmount();
		if (playerAmount < getMinPlayerAmount())
			throw new IllegalStateException("The Player amount is not enough to launch the game. Expect: " + getMinPlayerAmount() + ", actual: " + playerAmount);
		if (playerAmount > getMaxPlayerAmount())
			throw new IllegalStateException("The Player amount is out of the limit. Expect: " + getMaxPlayerAmount() + ", actual: " + playerAmount);
	}
	
	private void initGameAndStart(ClientPlayer hostClient, List<ClientPlayer> playerClients){
		switch (getGameMode()) {
		case DUEL1A2B:
			game = new Duel1A2BGame(protocolFactory, id, hostClient, playerClients.get(0));
			game.setLog(new ApacheLoggerAdapter(Duel1A2BGame.class));
			break;
		case GROUP1A2B:
			//TODO
			break;
		case DIXIT:
			//TODO
			break;
		}
		
		this.game.startGame();
		updateRoomAndPlayerStatusInGame(hostClient, playerClients);
	}
	
	private void updateRoomAndPlayerStatusInGame(ClientPlayer hostClient, List<ClientPlayer> playerClients){
		hostClient.getPlayer().setUserStatus(ClientStatus.inGame);
		for(ClientPlayer player : playerClients)
			player.getPlayer().setUserStatus(ClientStatus.inGame);
		setRoomStatus(RoomStatus.gamestarted);
	}
	
	public Game getGameModel() {
		return game;
	}
	
	@Override
	public String toString() {
		return String.format("Room id: %s, name: %s, GameMode: %s, Host: %s, Players: %d/%d, Status: %s", 
				id, name, gameMode.toString(), host.getName(), getPlayers().size(), gameMode.getMaxPlayerAmount(), roomStatus.toString());
	}
}
