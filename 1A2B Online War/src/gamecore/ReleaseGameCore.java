package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Linq.Linq;
import container.Client;
import container.protocol.Protocol;
import gamecore.entity.Room;
import gamecore.entity.User;
import gamefactory.GameFactory;

public class ReleaseGameCore implements GameCore{
	private GameFactory factory;
	private List<User> userContainer = Collections.checkedList(new ArrayList<>(), User.class);
	private List<Room> roomContainer = Collections.checkedList(new ArrayList<>(), Room.class);
	private Map<User, Client> clientsMap = Collections.checkedMap(new HashMap<>(), User.class, Client.class);
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public void notifyRoom(String roomId, Protocol response) {
		Room room = getRoom(roomId);
		List<User> users = room.getUsers();
		respondToServices(getUserServicesByUserList(users), response);
	}

	@Override
	public void notifyUser(String userId, Protocol response) {
		User user = getUser(userId);
		clientsMap.get(user).respond(response);
	}

	@Override
	public void notifyUsers(UserStatus status, Protocol response) {
		List<User> users = getUsers(status);
		respondToServices(getUserServicesByUserList(users), response);
	}
	
	private void respondToServices(List<Client> userServices, Protocol response){
		for (Client service : userServices)
			service.respond(response);
	}
	
	private List<Client> getUserServicesByUserList(List<User> users){
		List<Client> userServices = new ArrayList<>();
		for (User user : users)
			userServices.add(clientsMap.get(user));
		return userServices;
	}

	@Override
	public List<User> getUsers(UserStatus status) {
		return Linq.From(userContainer).where(u->u.getUserStatus() == status).toList();
	}

	@Override
	public List<Room> getRooms(String name) {
		return Linq.From(roomContainer).where(r->r.getName().equals(name)).toList();
	}

	@Override
	public List<Room> getRooms(RoomStatus status) {
		return Linq.From(roomContainer).where(r->r.getRoomStatus() == status).toList();
	}

	@Override
	public User getUser(String id) {
		return Linq.From(userContainer).single(u->u.getId().equals(id));
	}

	@Override
	public Room getRoom(String id) {
		return Linq.From(roomContainer).single(u->u.getId().equals(id));
	}

	@Override
	public List<User> getUserContainer() {
		return userContainer;
	}

	@Override
	public List<Room> getRoomContainer() {
		return roomContainer;
	}

}
