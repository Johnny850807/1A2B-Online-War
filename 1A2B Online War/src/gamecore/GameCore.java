package gamecore;

import java.util.List;
import java.util.Map;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.RoomStatus;
import gamecore.model.UserStatus;
import gamecore.rooms.RoomCore;

public interface GameCore {
	void notifyRoom(String roomId, Protocol response);
	void notifyUser(String userId, Protocol response);
	void notifyUsers(UserStatus userStatus, Protocol response);
	Map<Player, Client> getClientsMap();
	List<GameRoom> getRoomContainer();
	List<Player> getUsers(UserStatus status);
	List<GameRoom> getRooms(String name);
	List<GameRoom> getRooms(RoomStatus status);
	Player getUser(String id);
	GameRoom getRoom(String id);
	void removeUser(Player user);
	void removeClient(Client client);
}
