package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.UserStatus;
import gamecore.rooms.RoomCore;
import gamecore.rooms.RoomFactory;

public class CreateRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{

	public CreateRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom data) {
		if (data.getName() == null || data.getName().isEmpty() || data.getName().length() > 9)
			return error(101, new IllegalArgumentException("The room's name should be in the range(1~9)"));
		if (data.getHost() == null)
			return error(102, new IllegalArgumentException("The room should be hosted by a player."));
		if (data.getGameMode() == null)
			return error(103, new IllegalArgumentException("The room should be given a game mode."));
		
		data.initId();
		gameCore().getRoomContainer().add(data);
		return success(data);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().notifyUsers(UserStatus.SignedIn, responseProtocol);
	}

}
