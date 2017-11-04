package gamecore;

import java.util.List;

import container.UserService;
import container.protocol.Protocol;
import gamecore.entity.Room;
import gamecore.entity.User;

public interface GameCore {
	User signIn(UserService service, String name);
	void notifyRoom(String roomId, Protocol response);
	void notifyUser(String userId, Protocol response);
	void notifyUsers(String userId, Protocol response);
	List<User> getUsers(UserStatus userStatus, Protocol response);
	List<User> getUsers(UserStatus status);
	List<Room> getRooms();
	List<Room> getRooms(String name);
	List<Room> getRooms(RoomStatus status);
	User getUser(String id);
	Room getRoom(String id);
}
