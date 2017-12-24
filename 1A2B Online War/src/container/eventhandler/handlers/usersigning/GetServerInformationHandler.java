package container.eventhandler.handlers.usersigning;

import java.util.List;

import container.Constants;
import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientPlayer;
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
		ServerInformation information = new ServerInformation(Constants.VERSION, players.size(), 
				rooms.size());
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
