package container.eventhandler.handlers.inroom;

import container.base.Client;

import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;

/**
 * @author Johnny850807
 * Input: the closing room.
 * Output: (RoomList / InRoom) the closed room.
 */
public class CloseRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{

	public CloseRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom room) {
		if (room.getId() == null)
			return error(404, new IllegalArgumentException());
		gameCore().closeGameRoom(room);
		return success(room);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastClientPlayers(ClientStatus.signedIn, responseProtocol);
	}

}
