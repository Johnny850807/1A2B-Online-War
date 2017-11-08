package container.eventhandler.handlers;

import java.util.List;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.UserStatus;

public class GetRoomsHandler extends GsonEventHandler<Void, List<GameRoom>>{

	public GetRoomsHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<Void> getDataClass() {
		return Void.class;
	}

	@Override
	protected Response onHandling(Void data) {
		return success(gameCore().getRoomContainer());
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().notifyUsers(UserStatus.SignedIn, responseProtocol);
	}

}
