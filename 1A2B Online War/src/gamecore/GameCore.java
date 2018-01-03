package gamecore;

import java.util.List;

import container.base.Client;
import container.base.ClientBinder;
import container.protocol.Protocol;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;

public interface GameCore extends ClientBinder, Game.GameLifecycleListener{
	void broadcastRoom(String roomId, Protocol response);
	void broadcastClientPlayer(String userId, Protocol response);
	void broadcastClientPlayers(ClientStatus userStatus, Protocol response);
	
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
	void closeGameRoom(GameRoom room, Protocol protocol);
	void removePlayerFromRoomAndBroadcast(Player player, GameRoom gameRoom);
}
