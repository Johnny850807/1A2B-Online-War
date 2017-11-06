package container.eventhandler.handlers;

import java.util.List;
import java.util.Set;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ServerInformation;
import gamecore.rooms.RoomCore;

public class GetServerInformationHandler extends GsonEventHandler<Void, ServerInformation>{

	public GetServerInformationHandler(Client client, Protocol request, GameCore gameCore,
			ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<Void> getDataClass() {
		return Void.class;
	}

	@Override
	protected Response onHandling(Void data) {
		List<GameRoom> rooms = gameCore().getRoomContainer();
		Set<Player> users = gameCore().getClientsMap().keySet();
		ServerInformation information = new ServerInformation(users.size(), rooms.size());
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
