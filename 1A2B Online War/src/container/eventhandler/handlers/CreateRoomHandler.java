package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;

public class CreateRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{

	public CreateRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom room) {
		if (room.getName() == null || room.getName().isEmpty() || room.getName().length() > 9)
			return error(101, new IllegalArgumentException("The room's name should be in the range(1~9)"));
		if (room.getHost() == null)
			return error(102, new IllegalArgumentException("The room should be hosted by a player."));
		if (room.getGameMode() == null)
			return error(103, new IllegalArgumentException("The room should be given a game mode."));
		
		room.initId();
		gameCore().addGameRoom(room);
		return success(room);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().notifyClientPlayers(ClientStatus.signedIn, responseProtocol);
	}

}
