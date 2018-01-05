package container.eventhandler.handlers.inroom;

import container.core.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;

/**
 * @author Johnny850807
 * Input: the closing room.
 * Output: (RoomList / InRoom) the closed room.
 */
public class CloseRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{
	private String roomId;
	private GameRoom gameRoom;
	public CloseRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom room) {
		try{
			roomId = room.getId();
			this.gameRoom = gameCore().getGameRoom(roomId);
			gameCore().closeGameRoom(gameRoom);
			gameCore().getClientPlayer(client().getId()).pushLeisureTime();
			return success(gameRoom);
		}catch (NullPointerException e) {
			return error(404, e);
		}catch (IllegalArgumentException e) {
			return error(400, e);
		}
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		// the gamecore has handled it.
	}

}
