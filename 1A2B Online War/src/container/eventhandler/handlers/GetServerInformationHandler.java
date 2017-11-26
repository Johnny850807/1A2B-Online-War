package container.eventhandler.handlers;

import java.util.List;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.ClientPlayer;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ServerInformation;

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
		List<GameRoom> rooms = gameCore().getGameRooms();
		List<ClientPlayer> players = gameCore().getClientPlayers();
		ServerInformation information = new ServerInformation(players.size(), rooms.size());
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
