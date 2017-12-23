package container.eventhandler.handlers.inroom;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.GameMode;

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
		roomId = data.getId();
		GameRoom gameRoom = gameCore().getGameRoom(roomId);
		gameRoom.launchGame();
		return success(gameRoom);
	}


	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastClientPlayers(ClientStatus.signedIn, responseProtocol);
		gameCore().broadcastRoom(roomId, responseProtocol);
	}

}
