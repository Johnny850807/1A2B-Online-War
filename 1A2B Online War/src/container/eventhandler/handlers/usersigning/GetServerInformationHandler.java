package container.eventhandler.handlers.usersigning;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.core.Client;
import container.core.Constants;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.ReleaseGameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientPlayer;
import gamecore.model.ServerInformation;
import utils.LogHelper;

public class GetServerInformationHandler extends GsonEventHandler<Void, ServerInformation>{
	private static final Logger log = LogManager.getLogger(ReleaseGameCore.class);
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
		log.debug("Info of gamecore:");
		log.debug("Clients: " + LogHelper.clientsToString(gameCore().getClientPlayers()));
		log.debug("Rooms: " + LogHelper.roomsToString(gameCore().getGameRooms()));
		return success(information);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
