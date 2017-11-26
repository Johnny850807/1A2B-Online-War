package gamecore;

import java.util.List;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;
import gamecore.model.RoomStatus;
import gamecore.rooms.games.Game;

public interface GameCore {
	void notifyAllClientPlayersInRoom(String roomId, Protocol response);
	void notifyClientPlayer(String userId, Protocol response);
	void notifyClientPlayers(ClientStatus userStatus, Protocol response);
	
	List<ClientPlayer> getClientPlayers(ClientStatus status);
	List<ClientPlayer> getClientPlayers();
	List<GameRoom> getGameRooms(RoomStatus status);
	List<GameRoom> getGameRooms();
	
	ClientPlayer getClientPlayer(String id);
	GameRoom getGameRoom(String id);
	
	void addBindedClientPlayer(Client client, Player player);
	void removeClientPlayer(String id);
	void addGameRoom(GameRoom room);
	void closeGameRoom(GameRoom room);
	
	Game getGame(String gameRoomId);
	Game luanchGame(GameRoom gameRoom);
}
