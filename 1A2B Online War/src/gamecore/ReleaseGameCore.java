package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import container.Constants.Events.Games;
import container.Constants.Events.InRoom;
import container.Constants.Events.RoomList;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;
import gamecore.model.games.a1b2.GameOverModel;
import gamefactory.GameFactory;

/**
 * @author Waterball
 * ReleaseGameCore manages all the users, rooms and the client sockets binding to the users. All 
 * methods with the name 'notify' used for sending a response to certain clients.
 */
public class ReleaseGameCore implements GameCore{
	private static Logger log = LogManager.getLogger(ReleaseGameCore.class);
	private GameFactory factory;
	private Gson gson = new Gson();
	private Map<String, GameRoom> roomContainer = Collections.synchronizedMap(new LinkedHashMap<String, GameRoom>()); // <id, GameRoom>
	private Map<String, ClientPlayer> clientsMap = Collections.synchronizedMap(new HashMap<String, ClientPlayer>()); // <id, ClientPlayer>
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public void broadcastRoom(String roomId, Protocol response) {
		GameRoom room = getGameRoom(roomId);
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
		if (room.getId() == null || room.getProtocolFactory() == null)
			log.error("The room's id or the factory has not been initialized.");
		Protocol protocol = factory.getProtocolFactory().createProtocol(RoomList.CREATE_ROOM,
				RequestStatus.success.toString(), gson.toJson(room));
		
		if (clientsMap.containsKey(room.getHost().getId()))
		{
			broadcastClientPlayers(ClientStatus.signedIn, protocol);
			broadcastClientPlayer(room.getHost().getId(), protocol);
			roomContainer.put(room.getId(), room);
		}
		else
			log.error("The host of the added room not exists!");
	}
	
	@Override
	public void closeGameRoom(GameRoom room){
		room = getGameRoom(room.getId());
		Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.CLOSE_ROOM,
				RequestStatus.success.toString(), gson.toJson(room));
		broadcastRoom(room.getId(), protocol);
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		removeTheRoomSync(room, "Room removed: " + room);
	}
	
	@Override
	public void addBindedClientPlayer(Client client, Player player){
		if (player.getId() == null)
			throw new IllegalStateException("The player's id has not been initialized.");
		if (clientsMap.containsKey(client.getId()))
			throw new IllegalStateException("The id is duplicated from the new binded clientplayer !");
		
		ClientPlayer clientPlayer = new ClientPlayer(client, player);
		clientsMap.put(clientPlayer.getId(), clientPlayer);
		log.trace("Client added: " + clientPlayer);
	}

	@Override
	public void removeClientPlayer(String id) {
		if (clientsMap.containsKey(id))
		{
				
			ClientPlayer clientPlayer = clientsMap.get(id);
			log.trace("Client removing: " + clientPlayer);
			handleThePlayerRemovedEventToRooms(clientPlayer.getPlayer());
			clientsMap.remove(id);
			log.trace("Remove the player from the clientsMap.");
		}
		else
			log.error("The client didn't sign.");
	}
	
	/**
	 * Handle the operation of removing the player from any game room if exists, depends on two situation
	 * (1) the player is a host: close his room and broadcast the close event to the room.
	 * (2) the player is inside the room but not a host: boot him out from the room and broadcast the leave event to the room.
	 * @param player removed player
	 * @return if the player is in any room
	 */
	private void handleThePlayerRemovedEventToRooms(Player player){
		log.trace("Handling the player removed.");
		getGameRooms().parallelStream().forEach(gameRoom -> {
			synchronized (gameRoom) {
				if (roomContainer.containsKey(gameRoom.getId()) && gameRoom.containsPlayer(player))
					removeThePlayerFromTheRoomOrCloseHisRoom(player, gameRoom);
			}
		});
	}

	private void removeThePlayerFromTheRoomOrCloseHisRoom(Player player, GameRoom gameRoom){
		if (gameRoom.getHost().equals(player))
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
		gameRoom = getGameRoom(gameRoom.getId());
		synchronized(gameRoom)
		{
			if (gameRoom.containsPlayer(player))
			{
				gameRoom.removePlayer(player);
				broadcastClientPlayers(ClientStatus.signedIn, protocol);
				broadcastRoom(gameRoom.getId(), protocol);
			}
		}
	}

	@Override
	public void onGameStarted(Game game) {
		Protocol protocol = factory.getProtocolFactory().createProtocol(Games.GAMESTARTED,
				RequestStatus.success.toString(), null);
		broadcastRoom(game.getRoomId(), protocol);
	}

	@Override
	public void onGameInterrupted(Game game, ClientPlayer noResponsePlayer) {
		GameRoom room = getGameRoom(game.getRoomId());
		removeTheRoomSync(room, "Game interrupted, player " + noResponsePlayer.getPlayerName() + " disconntects.");
	}

	@Override
	public void onGameOver(Game game, GameOverModel gameOverModel) {
		GameRoom room = getGameRoom(game.getRoomId());
		removeTheRoomSync(room, "Game over, the room " + room.getName() + " closed.");
	}

	private void removeTheRoomSync(GameRoom room, String logMsg){
		synchronized (room) {
			if (roomContainer.containsKey(room.getId()))
			{
				log.trace(logMsg);
				roomContainer.remove(room.getId());
			}
		}
	}
}
