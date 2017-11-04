package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import container.UserService;
import container.protocol.Protocol;
import gamecore.entity.Room;
import gamecore.entity.User;
import gamefactory.GameFactory;

public class ReleaseGameCore implements GameCore{
	private GameFactory factory;
	private List<User> onlineUsers = Collections.checkedList(new ArrayList<>(), User.class);
	private List<Room> roomList = Collections.checkedList(new ArrayList<>(), Room.class);
	
	public ReleaseGameCore(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public User signIn(UserService service, String name) {
		return null;
	}

	@Override
	public void notifyRoom(String roomId, Protocol response) {
		
	}

	@Override
	public void notifyUser(String userId, Protocol response) {
		
	}

	@Override
	public void notifyUsers(String userId, Protocol response) {
	}

	@Override
	public List<User> getUsers(UserStatus userStatus, Protocol response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers(UserStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Room> getRooms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Room> getRooms(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Room> getRooms(RoomStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room getRoom(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
