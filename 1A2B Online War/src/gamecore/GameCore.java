package gamecore;

import java.util.List;

import container.Client;
import container.protocol.Protocol;
import gamecore.entity.Room;
import gamecore.entity.User;

public interface GameCore {
	void notifyRoom(String roomId, Protocol response);
	void notifyUser(String userId, Protocol response);
	void notifyUsers(UserStatus userStatus, Protocol response);
	List<User> getUserContainer();
	List<Room> getRoomContainer();
	List<User> getUsers(UserStatus status);
	List<Room> getRooms(String name);
	List<Room> getRooms(RoomStatus status);
	User getUser(String id);
	Room getRoom(String id);
}
