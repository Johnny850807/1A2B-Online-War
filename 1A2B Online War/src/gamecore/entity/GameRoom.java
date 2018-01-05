package gamecore.entity;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import container.core.ClientBinder;
import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.ApacheLoggerAdapter;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.GameMode;
import gamecore.model.MockLogger;
import gamecore.model.PlayerStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;
import gamecore.model.games.GameEnteringWaitingBox;
import gamecore.model.games.a1b2.boss.imp.BasicBoss;
import gamecore.model.games.a1b2.boss.imp.Boss1A2BGame;
import gamecore.model.games.a1b2.duel.imp.Duel1A2BGame;
import utils.ForServer;
import utils.LogHelper;

/**
 * GameRoom contains only the info and the status the room should present. The game of the room will be
 * created by a GameMode.
 */
public class GameRoom extends Entity implements LeisureTimeChallengeable{
	private transient ProtocolFactory protocolFactory;
	private transient MyLogger log = new MockLogger();
	private transient Game game;
	private transient ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
	public static final long LEISURE_TIME_EXPIRE = TimeUnit.MINUTES.toMillis(3);
	private Date createdTime;
	private Date latestLeisureTime;
	private Player host;
	private RoomStatus roomStatus = RoomStatus.waiting;
	private GameMode gameMode;
	
	/**
	 * All the guest player status in the room, except the host.
	 */
	private List<PlayerStatus> playerStatusList =  Collections.checkedList(new ArrayList<>(), PlayerStatus.class);
	private String name;

	public GameRoom(GameMode gameMode, String name, Player host) {
		this.gameMode = gameMode;
		this.name = name;
		this.host = host;
		this.host.setUserStatus(ClientStatus.inRoom);
	}
	
	@ForServer
	public void addChatMessage(ChatMessage chatMessage){
		getChatMessageList().add(chatMessage);
		pushLeisureTime();
	}
	
	@ForServer
	public int getChatMessagesSize(){
		return getChatMessageList().size();
	}
	
	@ForServer
	public List<ChatMessage> getChatMessageList() {
		return chatMessageList == null ? chatMessageList = new ArrayList<>() : chatMessageList;
	} 
	
	@ForServer
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
		this.latestLeisureTime = createdTime;
	}
	
	@Override
	@ForServer
	public synchronized boolean isLeisureTimeExpired(){
		long diffTime = System.currentTimeMillis() - latestLeisureTime.getTime();
		return diffTime >= LEISURE_TIME_EXPIRE;
	}

	@Override
	@ForServer
	public void pushLeisureTime(){
		latestLeisureTime = new Date();
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
	
	public boolean isHost(Player player){
		return player.equals(getHost());
	}

	public void setHost(Player host) {
		this.host = host;
	}

	public synchronized RoomStatus getRoomStatus() {
		validateIdNull();
		return roomStatus;
	}

	public synchronized void setRoomStatus(RoomStatus roomStatus) {
		System.out.println("Set room status: " + roomStatus);
		validateIdNull();
		this.roomStatus = roomStatus;
	}

	public synchronized List<PlayerStatus> getPlayerStatus() {
		return playerStatusList;
	}
	
	public synchronized int getPlayerAmount(){
		return getPlayers().size(); 
	}
	
	public synchronized void changePlayerStatus(Player player, boolean ready){
		getPlayerStatusOfPlayer(player).setReady(ready);
		pushLeisureTime();
	}
	
	public synchronized Player getPlayerById(String playerId){
		for (PlayerStatus playerStatus : playerStatusList)
			if (playerStatus.getPlayer().getId().equals(playerId))
				return playerStatus.getPlayer();
		return null;
	}
	
	/**
	 * @return all the players including the host at the first position.
	 */
	public synchronized List<Player> getPlayers(){
		List<Player> players = new ArrayList<>();
		if (host != null)
			players.add(host);
		for (PlayerStatus playerStatus : playerStatusList)
			players.add(playerStatus.getPlayer());
		return players;
	}

	public synchronized void addPlayer(Player player){
		player.setUserStatus(ClientStatus.inRoom);
		PlayerStatus playerStatus = new PlayerStatus(player);
		validateNewPlayer(playerStatus);
		playerStatusList.add(playerStatus);
		pushLeisureTime();
	}
	
	private void validateNewPlayer(PlayerStatus playerStatus){
		if (host.equals(playerStatus.getPlayer()) || playerStatusList.contains(playerStatus))
			throw new IllegalStateException("Duplicated player added into the status list.");
		if (getPlayerAmount() == getMaxPlayerAmount())
			throw new IllegalStateException("The Player amount is out of the maximum amount.");
	}
	
	public synchronized void removePlayer(Player player){
		if (player.equals(host))
			host = null;
		else
			playerStatusList.remove(getPlayerStatusOfPlayer(player));
	}
	
	public synchronized PlayerStatus getPlayerStatusOfPlayer(Player player){
		for (PlayerStatus playerStatus : playerStatusList)
			if(playerStatus.getPlayer().equals(player))
				return playerStatus;
		throw new IllegalStateException("The removed player doesn't exist in the room !");
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public synchronized int getMaxPlayerAmount(){
		return gameMode.getMaxPlayerAmount();
	}
	
	public synchronized int getMinPlayerAmount(){
		return gameMode.getMinPlayerAmount();
	}
	
	public synchronized boolean containsPlayer(Player player){
		return (host != null && host.equals(player)) || ifPlayerInStatusList(player);
	}
	
	public void validateIdNull(){
		if (getId() == null)
			throw new IllegalStateException("The game room's id is null.");
	}
	
	public synchronized boolean ifPlayerInStatusList(Player player){
		try{
			getPlayerStatusOfPlayer(player);
			return true;
		}catch (IllegalStateException e) {
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
	@ForServer
	public synchronized void launchGame(ClientBinder clientBinder, Game.GameLifecycleListener listener){
		log.trace("Room: " + id + ", launcing the " + gameMode.toString() + " game.");
		validatePlayerAmount();
		
		ClientPlayer hostClient = clientBinder.getClientPlayer(host.getId());
		List<ClientPlayer> playerClients = new ArrayList<>();
		for(PlayerStatus playerStatus : playerStatusList)
			playerClients.add(clientBinder.getClientPlayer(playerStatus.getPlayer().getId()));
		
		log.trace("Host prepared: " + hostClient.getPlayerName());
		log.trace("Players prepared: " + LogHelper.clientsToString(playerClients));
		
		initGame(hostClient, playerClients, listener);
		pushLeisureTime();
	}
	
	public synchronized boolean canStartTheGame(){
		int playerAmount = getPlayerAmount();
		return playerAmount >= getMinPlayerAmount() && playerAmount <= getMaxPlayerAmount()
				&& host != null && areAllPlayerReady();
	}
	
	public synchronized boolean areAllPlayerReady(){
		for (PlayerStatus playerStatus : playerStatusList)
			if (!playerStatus.isReady())
				return false;
		return true;
	}
	
	public synchronized boolean isAvailableToJoin(){
		return getPlayerAmount() != getMaxPlayerAmount() && 
				getRoomStatus() != RoomStatus.gamestarted;
	}
	
	private void validatePlayerAmount(){
		int playerAmount = getPlayerAmount();
		if (playerAmount < getMinPlayerAmount())
			throw new IllegalStateException("The Player amount is not enough to launch the game. Expect: " + getMinPlayerAmount() + ", actual: " + playerAmount);
		if (playerAmount > getMaxPlayerAmount())
			throw new IllegalStateException("The Player amount is out of the limit. Expect: " + getMaxPlayerAmount() + ", actual: " + playerAmount);
	}
	
	private void initGame(ClientPlayer hostClient, List<ClientPlayer> playerClients, Game.GameLifecycleListener listener){
		List<ClientPlayer> allPlayers = new ArrayList<>();
		allPlayers.add(hostClient);
		allPlayers.addAll(playerClients);
		
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
		case BOSS1A2B:
			game = new Boss1A2BGame(protocolFactory, new BasicBoss(new ApacheLoggerAdapter(BasicBoss.class), protocolFactory), 
					allPlayers, id);
			game.setLog(new ApacheLoggerAdapter(Boss1A2BGame.class));
			break;
		}
		
		game.setGameLifecycleListener(listener);
		game.setEnteringWaitingBox(new GameEnteringWaitingBox(game, allPlayers));
		updateRoomAndPlayerStatusInGame(hostClient, playerClients);
	}
	
	private void updateRoomAndPlayerStatusInGame(ClientPlayer hostClient, List<ClientPlayer> playerClients){
		setAllPlayerStatus(ClientStatus.inGame);
		setRoomStatus(RoomStatus.gamestarted);
	}
	
	public void setAllPlayerStatus(ClientStatus status){
		for (Player player : getPlayers())
			player.setUserStatus(status);
	}

	/**
	 * only the operations to the game will invoke getGame() method by the handler,
	 * so getGame() will push the room's leisure time. 
	 */
	@ForServer
	public Game getGame() {
		pushLeisureTime();
		return game;
	}
	
	@Override
	public String toString() {
		return String.format("Room id: %s, name: %s, GameMode: %s, Host: %s, Players: %d/%d, Status: %s", 
				id, name, gameMode.toString(), host == null ? "null" : host.getName(), getPlayers().size(), gameMode.getMaxPlayerAmount(), roomStatus.toString());
	}

}
