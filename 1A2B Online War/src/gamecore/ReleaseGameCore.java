package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Linq.Linq;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.RoomStatus;
import gamecore.model.ClientStatus;
import gamecore.rooms.RoomCore;
import gamecore.rooms.games.Game;
import gamefactory.GameFactory;

/**
 * @author AndroidWork
 * ReleaseGameCore manages all the users, rooms and the client sockets binding to the users. All 
 * methods with the name 'notify' used for sending a response to certain clients.
 */
public class ReleaseGameCore implements GameCore{
	private GameFactory factory;
	private Map<String, GameRoom> roomContainer = Collections.checkedMap(new LinkedHashMap<>(), String.class, GameRoom.class); // <id, GameRoom>
	private Map<String, ClientPlayer> clientsMap = Collections.checkedMap(new HashMap<>(), String.class, ClientPlayer.class); // <id, ClientPlayer>
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public void notifyAllClientPlayersInRoom(String roomId, Protocol response) {
		GameRoom room = getGameRoom(roomId);
		List<Player> players = room.getPlayers();
		respondToClients(getClientsByPlayerList(players), response);
	}

	private List<ClientPlayer> getClientsByPlayerList(List<Player> players) {
		return Linq.From(clientsMap.values()).where(c -> players.contains(c.getPlayer())).toList();
	}

	@Override
	public void notifyClientPlayer(String userId, Protocol response) {
		ClientPlayer clientPlayer = getClientPlayer(userId);
		clientPlayer.respondToClient(response);
	}

	@Override
	public void notifyClientPlayers(ClientStatus status, Protocol response) {
		List<ClientPlayer> clientPlayers = getClientPlayers(status);
		respondToClients(clientPlayers, response);
	}
	
	private void respondToClients(List<ClientPlayer> clientPlayers, Protocol response){
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
		roomContainer.put(room.getId(), room);
	}
	
	@Override
	public void closeGameRoom(GameRoom room){
		roomContainer.remove(room.getId());
	}
	
	@Override
	public void addBindedClientPlayer(Client client, Player player){
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
		}
		else
			throw new IllegalStateException("The client wasn't signed.");
	}

}
