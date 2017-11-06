package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.UserStatus;

public class CloseRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{

	public CloseRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom data) {
		if (data.getId() == null)
			return error(404, new IllegalArgumentException());
		gameCore().getRoomContainer().remove(data);
		return success(data);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().notifyUsers(UserStatus.SignedIn, responseProtocol);
	}

}
