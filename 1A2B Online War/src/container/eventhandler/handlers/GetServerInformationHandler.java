package container.eventhandler.handlers;

import java.util.List;
import java.util.Set;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Player;
import gamecore.model.ServerInformation;
import gamecore.rooms.RoomCore;

public class GetServerInformationHandler extends GsonEventHandler<ServerInformation>{

	public GetServerInformationHandler(Client client, Protocol request, GameCore gameCore,
			ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ServerInformation> getDataClass() {
		return ServerInformation.class;
	}

	@Override
	protected Response onHandling(ServerInformation data) {
		List<RoomCore> rooms = gameCore().getRoomContainer();
		Set<Player> users = gameCore().getClientsMap().keySet();
		ServerInformation information = new ServerInformation(users.size(), rooms.size());
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
