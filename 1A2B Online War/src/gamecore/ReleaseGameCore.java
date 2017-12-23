package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import Linq.Linq;
import container.Constants.Events.InRoom;
import container.Constants.Events.RoomList;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
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
		return Linq.From(clientsMap.values()).where(c -> players.contains(c.getPlayer())).toList();
	}

	@Override
	public void broadcastClientPlayer(String userId, Protocol response) {
		ClientPlayer clientPlayer = getClientPlayer(userId);
		clientPlayer.respondToClient(response);
	}

	@Override
	public void broadcastClientPlayers(ClientStatus status, Protocol response) {
		List<ClientPlayer> clientPlayers = getClientPlayers(status);
		broadcastToClients(clientPlayers, response);
	}
	
	private void broadcastToClients(List<ClientPlayer> clientPlayers, Protocol response){
		for (ClientPlayer clientPlayer : clientPlayers)
			clientPlayer.respondToClient(response);
	}

	@Override
	public List<ClientPlayer> getClientPlayers(ClientStatus status){
		return Linq.From(clientsMap.values()).where(c->c.getPlayerStatus() == status).toList();
	}
	
	@Override
	public List<ClientPlayer> getClientPlayers(){
		return new ArrayList<>(clientsMap.values());
	}
	
	@Override
	public List<GameRoom> getGameRooms(RoomStatus status) {
		return Linq.From(roomContainer.values()).where(r->r.getRoomStatus() == status).toList();
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
		if (room.getId() == null)
			throw new IllegalArgumentException("The room's id has not been initialized.");
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
		System.out.println("== Room removed ==\n" + room + "====================");
	}
	
	@Override
	public void addBindedClientPlayer(Client client, Player player){
		if (player.getId() == null)
			throw new IllegalArgumentException("The player's id has not been initialized.");
		ClientPlayer clientPlayer = new ClientPlayer(client, player);
		assert !clientsMap.containsKey(clientPlayer.getId()) : "The id is duplicated from the new binded clientplayer !";
		clientsMap.put(clientPlayer.getId(), clientPlayer);
		System.out.println("== Client added ==\n" + clientPlayer +"====================");
	}

	@Override
	public void removeClientPlayer(String id) {
		if (clientsMap.containsKey(id))
		{
			ClientPlayer clientPlayer = clientsMap.remove(id);
			System.out.println("== Client removed ==\n" + clientPlayer +"====================");
			handleThePlayerRemoved(clientPlayer.getPlayer());
		}
		else
			throw new IllegalStateException("The client wasn't signed.");
	}
	
	/**
	 * @param player removed player
	 * @return if the player is in any room
	 */
	private boolean handleThePlayerRemoved(Player player){
		for (GameRoom gameRoom : getGameRooms())
			if (gameRoom.containsPlayer(player))
			{
				if (gameRoom.getHost().equals(player))
					closeGameRoom(gameRoom);
				else
					removePlayerFromRoomAndBroadcast(player, gameRoom);
				return true;
			}
		return false;
	}


	@Override
	public void removePlayerFromRoomAndBroadcast(Player player, GameRoom gameRoom){
		Protocol protocol = factory.getProtocolFactory().createProtocol(InRoom.LEAVE_ROOM, 
				RequestStatus.success.toString(), gson.toJson(new PlayerRoomModel(player, gameRoom)));
		gameRoom.removePlayer(player);
		broadcastClientPlayers(ClientStatus.signedIn, protocol);
		broadcastRoom(gameRoom.getId(), protocol);
	}
}
