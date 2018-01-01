package container.eventhandler.handlers.inroom;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.RoomStatus;

/**
 * @author Johnny850807
 * Input: the launching room.
 * Output: (InRoom / RoomList) the launched room.
 */
public class LaunchGameHandler extends GsonEventHandler<GameRoom, GameRoom>{
	private String roomId;
	public LaunchGameHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom data) {
		try{
			roomId = data.getId();
			GameRoom gameRoom = gameCore().getGameRoom(roomId);
			validateLaunchingGame(gameRoom);
			gameRoom.launchGame(gameCore(), gameCore());
			return success(gameRoom);
		}catch (NullPointerException e) {
			return error(404, e);
		}catch (IllegalStateException|IllegalAccessException e){
			return error(403, e);
		}
	}

	private void validateLaunchingGame(GameRoom gameRoom) throws IllegalAccessException{
		if (gameRoom.getRoomStatus() == RoomStatus.gamestarted)
			throw new IllegalStateException("The gameroom has been started.");
		if (!client().getId().equals(gameRoom.getHost().getId()))
			throw new IllegalAccessException("You are not the host, how did you launch the game?");
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastRoom(roomId, responseProtocol);
		gameCore().broadcastClientPlayers(ClientStatus.signedIn, responseProtocol);
	}

}
