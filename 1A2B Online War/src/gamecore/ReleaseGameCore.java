package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Linq.Linq;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.Player;
import gamecore.model.RoomStatus;
import gamecore.model.UserStatus;
import gamecore.rooms.RoomCore;
import gamefactory.GameFactory;

/**
 * @author AndroidWork
 * ReleaseGameCore manages all the users, rooms and the client sockets binding to the users. All 
 * methods with the name 'notify' used for sending a response to certain clients.
 */
public class ReleaseGameCore implements GameCore{
	private GameFactory factory;
	private List<RoomCore> roomContainer = Collections.checkedList(new ArrayList<>(), RoomCore.class);
	private Map<Player, Client> clientsMap = Collections.checkedMap(new HashMap<>(), Player.class, Client.class);
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public void notifyRoom(String roomId, Protocol response) {
		RoomCore room = getRoom(roomId);
		List<Player> users = room.getUsers();
		respondToServices(getUserServicesByUserList(users), response);
	}

	@Override
	public void notifyUser(String userId, Protocol response) {
		Player user = getUser(userId);
		clientsMap.get(user).respond(response);
	}

	@Override
	public void notifyUsers(UserStatus status, Protocol response) {
		List<Player> users = getUsers(status);
		respondToServices(getUserServicesByUserList(users), response);
	}
	
	private void respondToServices(List<Client> userServices, Protocol response){
		for (Client service : userServices)
			service.respond(response);
	}
	
	private List<Client> getUserServicesByUserList(List<Player> users){
		List<Client> userServices = new ArrayList<>();
		for (Player user : users)
			userServices.add(clientsMap.get(user));
		return userServices;
	}

	@Override
	public List<Player> getUsers(UserStatus status) {
		return Linq.From(clientsMap.keySet()).where(u->u.getUserStatus() == status).toList();
	}

	@Override
	public List<RoomCore> getRooms(String name) {
		return Linq.From(roomContainer).where(r->r.getName().equals(name)).toList();
	}

	@Override
	public List<RoomCore> getRooms(RoomStatus status) {
		return Linq.From(roomContainer).where(r->r.getRoomStatus() == status).toList();
	}

	@Override
	public Player getUser(String id) {
		return Linq.From(clientsMap.keySet()).single(u->u.getId().equals(id));
	}

	@Override
	public RoomCore getRoom(String id) {
		return Linq.From(roomContainer).single(u->u.getId().equals(id));
	}
	
	@Override
	public Map<Player, Client> getClientsMap() {
		return clientsMap;
	}

	@Override
	public List<RoomCore> getRoomContainer() {
		return roomContainer;
	}

	@Override
	public void removeUser(Player user) {
		clientsMap.remove(user);
	}

	@Override
	public void removeClient(Client client) {
		Set<Player> users = clientsMap.keySet();
		
		for (Player user : users)
			if (clientsMap.get(user) == client)
			{
				clientsMap.remove(user);
				break;
			}
	}

}
