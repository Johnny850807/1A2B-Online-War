package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.validator.ValidateWith;

import com.google.gson.Gson;

import container.core.Client;
import container.core.Constants.Events.Games;
import container.core.Constants.Events.InRoom;
import container.core.Constants.Events.RoomList;
import container.core.Constants.Events.Signing;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;
import gamecore.model.games.GameOverModel;
import gamefactory.GameFactory;
import utils.ForServer;
import utils.MyGson;

/**
 * @author Waterball
 * ReleaseGameCore manages all the users, rooms and the client sockets binding to the users. All 
 * methods with the name 'notify' used for sending a response to certain clients.
 * 
 * this gamecore handles all game rooms and all client players.
 */
public class ReleaseGameCore implements GameCore{
	private static Logger log = LogManager.getLogger(ReleaseGameCore.class);
	private Challenger challenger = new Challenger();
	private GameFactory factory;
	private Gson gson = MyGson.getGson();
	private Map<String, GameRoom> roomContainer = Collections.synchronizedMap(new LinkedHashMap<String, GameRoom>()); // <id, GameRoom>
	private Map<String, ClientPlayer> clientsMap = Collections.synchronizedMap(new HashMap<String, ClientPlayer>()); // <id, ClientPlayer>
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
		challenger.start();
	}

	@Override
	public void broadcastRoom(String roomId, Protocol response) {
		GameRoom room = getGameRoom(roomId);
		validateNotNull(room);
		synchronized (room) {
			if (roomContainer.containsKey(room.getId()))
			{
				log.trace("Broadcasting room: " + room.getName() + ", event: " + response.getEvent());
				room.getPlayers().parallelStream().forEach(p -> broadcastClientPlayer(p.getId(), response));
				log.trace("Broadcasting room completed.");
			}
		}
	}

	@Override
	public void broadcastClientPlayer(String userId, Protocol response) {
		log.trace("Broadcasting client player by id: " + userId + ", event: " + response.getEvent());
		ClientPlayer clientPlayer = getClientPlayer(userId);
		clientPlayer.broadcast(response);
		log.trace("Broadcasting client completed.");
	}

	@Override
	public void broadcastClientPlayers(ClientStatus status, Protocol response) {
		log.trace("Broadcasting client players by status: " + status.toString() + ", event: " + response.getEvent());
		getClientPlayers().parallelStream().filter(cp -> cp.getPlayerStatus() == status)
			.forEach(cp -> cp.broadcast(response));
		log.trace("Broadcasting clients completed.");
	}
	
	@Override
	public List<ClientPlayer> getClientPlayers(ClientStatus status){
		return getClientPlayers().stream()
				.filter(c -> c.getPlayerStatus() == status).collect(Collectors.toList());
	}
	
	@Override
	public List<ClientPlayer> getClientPlayers(){
		return new ArrayList<>(clientsMap.values());
	}
	
	@Override
	public List<GameRoom> getGameRooms(RoomStatus status) {
		return getGameRooms().stream()
				.filter(r->r.getRoomStatus() == status).collect(Collectors.toList());
	}
	
	@Override
	public List<GameRoom> getGameRooms(){
		return new ArrayList<>(roomContainer.values());
	}
	
	@Override
	public ClientPlayer getClientPlayer(String id){
		return clientsMap.get(id);
	}

	@Override
	public GameRoom getGameRoom(String id) {
		return roomContainer.get(id);
	}
	
	@Override
	public void addGameRoom(GameRoom room){
		if (room.getId() == null || room.getProtocolFactory() == null || room.getHost() == null)
			throw new NullPointerException("The room is invalid.");
		Protocol protocol = factory.getProtocolFactory().createProtocol(RoomList.CREATE_ROOM,
				RequestStatus.success.toString(), gson.toJson(room));
		
		if (clientsMap.containsKey(room.getHost().getId()))
		{
			broadcastClientPlayers(ClientStatus.signedIn, protocol);
			roomContainer.put(room.getId(), room);
			ClientPlayer hostClient = getClientPlayer(room.getHost().getId());
			hostClient.getPlayer().setUserStatus(ClientStatus.inRoom);
		}
		else
			log.error("The host of the added room not exists!");
	}
	
	@Override
	public void closeGameRoom(GameRoom room) {
		closeGameRoom(room, factory.getProtocolFactory().createProtocol(InRoom.CLOSE_ROOM,
				RequestStatus.success.toString(), gson.toJson(room)));
	}
	
	@Override
	public void closeGameRoom(GameRoom room, Protocol protocol){
		room = getGameRoom(room.getId());  
		validateNotNull(room);
		
		//first change all the player status in the room to signedIn.
		room.setAllPlayerStatus(ClientStatus.signedIn);
		
		/*then broadcast to all the signedIn players, for each player just got out from the room,
		they will also receive the room closed event*/
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		removeTheRoomFromMapSync(room, "Room removed: " + room);
	}
	
	@Override
	public void addBindedClientPlayer(Client client, Player player){
		if (player.getId() == null)
			throw new NullPointerException("The player's id has not been initialized.");
		if (clientsMap.containsKey(client.getId()))
			throw new IllegalStateException("The id is duplicated from the new binded clientplayer !");
		
		player.setUserStatus(ClientStatus.signedIn);
		ClientPlayer clientPlayer = new ClientPlayer(client, player);
		clientsMap.put(clientPlayer.getId(), clientPlayer);
		log.trace("Client added: " + clientPlayer);
	}

	@Override
	public void removeClientPlayer(String id) {
		if (clientsMap.containsKey(id))
		{
			ClientPlayer clientPlayer = clientsMap.get(id);
			synchronized (clientPlayer) 
			{
				if (clientsMap.containsKey(id))
				{
					log.trace("Client removing: " + clientPlayer);
					handleThePlayerRemovedEventToRooms(clientPlayer.getPlayer());
					clientsMap.remove(id);
					log.trace("Remove the player from the clientsMap.");
				}
			}
		}
		else
			log.error("The client didn't sign.");
	}
	
	/**
	 * Handle the operation of removing the player from any game room if exists, depends on two situation
	 * (1) the player is a host: close his room and broadcast the close event to the room.
	 * (2) the player is inside the room but not a host: boot him out from the room and broadcast the leave event to the room.
	 * @param player removed player
	 */
	private void handleThePlayerRemovedEventToRooms(final Player player){
		log.trace("Handling the player removed.");
		getGameRooms().parallelStream().forEach(gameRoom -> {
			synchronized (gameRoom) {
				if (roomContainer.containsKey(gameRoom.getId()) && gameRoom.containsPlayer(player))
					removeThePlayerFromTheRoomOrCloseHisRoom(player, gameRoom);
			}
		});
	}

	private void removeThePlayerFromTheRoomOrCloseHisRoom(Player player, GameRoom gameRoom){
		if (gameRoom.isHost(player))
		{
			log.trace("The player is the host from the room: " + gameRoom.getName() + ", closing his room.");
			closeGameRoom(gameRoom);
		}
		else
		{
			log.trace("The player is the player from the room: " + gameRoom.getName() + ", remove him from the room.");
			removePlayerFromRoomAndBroadcast(player, gameRoom);
		}
		log.trace("The player is inside the room, remove successfully.");
	}
	
	@Override
	public void removePlayerFromRoomAndBroadcast(Player player, GameRoom gameRoom){
		Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.LEAVE_ROOM, 
				RequestStatus.success.toString(), gson.toJson(new PlayerRoomModel(player, gameRoom)));
		ClientPlayer leftPlayer = getClientPlayer(player.getId());
		gameRoom = getGameRoom(gameRoom.getId());
		validateNotNull(leftPlayer, gameRoom);
		synchronized(gameRoom)
		{
			if (gameRoom.containsPlayer(leftPlayer.getPlayer()))
			{
				gameRoom.removePlayer(leftPlayer.getPlayer());
				broadcastRoom(gameRoom.getId(), protocol);
				broadcastClientPlayers(ClientStatus.signedIn, protocol);
				leftPlayer.getPlayer().setUserStatus(ClientStatus.signedIn);
			}
		}
	}

	@Override
	public void onGameStarted(Game game, Protocol gameStartedProtocol) {
		broadcastRoom(game.getRoomId(), gameStartedProtocol);
	}

	@Override
	public void onGameInterrupted(Game game, ClientPlayer noResponsePlayer) {
		GameRoom room = getGameRoom(game.getRoomId());
		log.trace("Game interrupted, player " + noResponsePlayer.getPlayerName() + " disconntects, closing it.");
		room.setAllPlayerStatus(ClientStatus.signedIn);
		room.setRoomStatus(RoomStatus.waiting);
		removeTheRoomFromMapSync(room, "The game is over.");
	}

	@Override
	public void onGameOver(Game game) {
		GameRoom room = getGameRoom(game.getRoomId());
		log.trace("Game over, the room " + room.getName() + " closed, closing it.");
		room.setAllPlayerStatus(ClientStatus.signedIn);
		room.setRoomStatus(RoomStatus.waiting);
		removeTheRoomFromMapSync(room, "The game is over.");
		System.gc();
	}

	private void removeTheRoomFromMapSync(GameRoom room, String logMsg){
		synchronized (room) {
			if (roomContainer.containsKey(room.getId()))
			{
				log.trace(logMsg);
				roomContainer.remove(room.getId());
			}
		}
	}
	
	private void removeTheClientPlayerFromMapSync(ClientPlayer clientPlayer){
		synchronized (clientPlayer) {
			if (clientsMap.containsKey(clientPlayer.getId()))
				clientsMap.remove(clientPlayer.getId());
		}
	}
	
	private void validateNotNull(Object ...objs){
		for (Object object : objs)
			if (object == null)
				throw new NullPointerException();
	}
	
	/**
	 * @author Waterball
	 * The challenger to challenge each game room. Close the room if it has been so much time on waiting.
	 */
	private class Challenger extends TimerTask{
		private Timer timer = new Timer();
		private static final long DELAY = 20000;
		private static final long PERIOD = 20000;
		
		public void start(){
			timer.schedule(this, DELAY, PERIOD);
		}
		
		@Override
		public void run() {
			challengingRooms();
			challengingClientPlayers();
		}
		
		private void challengingRooms(){
			getGameRooms().parallelStream()
			.forEach(room -> {
				if (room.isLeisureTimeExpired())
					shutdownTheRoomForExpired(room);
			});
		}
		
		private void shutdownTheRoomForExpired(GameRoom room){
			log.trace("Challenging- shut down the room: " + room);
			Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.CLOSE_ROOM_TIME_EXPIRED,
					RequestStatus.success.toString(), gson.toJson(room));
			closeGameRoom(room, protocol);
		}
		
		private void challengingClientPlayers(){
			getClientPlayers().parallelStream()
			.forEach(clientPlayer -> {
				if (clientPlayer.isLeisureTimeExpired() 
							&& clientPlayer.getPlayerStatus() == ClientStatus.signedIn)
					signOutThePlayer(clientPlayer);
			});
		}
		
		private void signOutThePlayer(ClientPlayer clientPlayer){
			log.trace("Challenging- sign out the player: " + clientPlayer);
			Protocol protocol = factory.getProtocolFactory().createProtocol(Signing.SIGNOUT_TIME_EXPIRED,
					RequestStatus.success.toString(), null);
			clientPlayer.broadcast(protocol);
			removeTheClientPlayerFromMapSync(clientPlayer);
		}
		
	}
}
