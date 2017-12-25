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

import Linq.Linq;
import container.Constants.Events.InRoom;
import container.Constants.Events.RoomList;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientBinder;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;
import gamecore.model.RoomStatus;
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
	private Map<String, GameRoom> roomContainer = Collections.checkedMap(new LinkedHashMap<>(), String.class, GameRoom.class); // <id, GameRoom>
	private Map<String, ClientPlayer> clientsMap = Collections.checkedMap(new HashMap<>(), String.class, ClientPlayer.class); // <id, ClientPlayer>
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public void broadcastRoom(String roomId, Protocol response) {
		GameRoom room = getGameRoom(roomId);
		List<Player> players = room.getPlayers();
		broadcastToClients(getClientsByPlayerList(players), response);
	}

	private List<ClientPlayer> getClientsByPlayerList(List<Player> players) {
		return players.stream()
				.map(p -> clientsMap.get(p.getId())).collect(Collectors.toList());
	}

	@Override
	public void broadcastClientPlayer(String userId, Protocol response) {
		ClientPlayer clientPlayer = getClientPlayer(userId);
		clientPlayer.broadcast(response);
	}

	@Override
	public void broadcastClientPlayers(ClientStatus status, Protocol response) {
		List<ClientPlayer> clientPlayers = getClientPlayers(status);
		broadcastToClients(clientPlayers, response);
	}
	
	private void broadcastToClients(List<ClientPlayer> clientPlayers, Protocol response){
		clientPlayers.stream().forEach(c -> c.broadcast(response));
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
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		roomContainer.put(room.getId(), room);
	}
	
	@Override
	public void closeGameRoom(GameRoom room){
		Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.CLOSE_ROOM,
				RequestStatus.success.toString(), gson.toJson(room));
		broadcastRoom(room.getId(), protocol);
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		roomContainer.remove(room.getId());
		log.trace("Room removed: " + room);
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
			handleThePlayerRemoved(clientPlayer.getPlayer());
			clientsMap.remove(id);
		}
		else
			log.error("The client wasn't signed.");
	}
	
	/**
	 * @param player removed player
	 * @return if the player is in any room
	 */
	private boolean handleThePlayerRemoved(Player player){
		log.trace("Handling the player removed");
		for (GameRoom gameRoom : getGameRooms())
			if (gameRoom.containsPlayer(player))
			{
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
				return true;
			}
		if (clientsMap.containsKey(player.getId()))
			clientsMap.remove(player.getId());
		return false;
	}


	@Override
	public void removePlayerFromRoomAndBroadcast(Player player, GameRoom gameRoom){
		gameRoom.removePlayer(player);
		Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.LEAVE_ROOM, 
				RequestStatus.success.toString(), gson.toJson(new PlayerRoomModel(player, gameRoom)));
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		broadcastRoom(gameRoom.getId(), protocol);
	}

	@Override
	public ClientBinder clientBinder() {
		return new ClientBinder() {
			@Override
			public ClientPlayer getClientPlayer(String playerId) {
				return ReleaseGameCore.this.getClientPlayer(playerId);
			}
		};
	}
}
