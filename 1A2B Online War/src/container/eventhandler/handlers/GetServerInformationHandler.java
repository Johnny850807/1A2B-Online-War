package container.eventhandler.handlers;

import java.util.List;

import container.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Room;
import gamecore.entity.User;
import gamecore.model.ServerInformation;

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
		List<Room> rooms = gameCore().getRoomContainer();
		List<User> users = gameCore().getUserContainer();
		ServerInformation information = new ServerInformation(users.size(), rooms.size());
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
