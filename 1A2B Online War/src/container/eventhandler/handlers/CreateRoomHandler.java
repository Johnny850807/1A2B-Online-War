package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.rooms.RoomCore;
import gamecore.rooms.RoomFactory;

public class CreateRoomHandler extends GsonEventHandler<GameRoom>{
	private Class<GameRoom> roomClass = null;
	public CreateRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	@Override
	protected Class<GameRoom> getDataClass() {
		return roomClass;
	}

	@Override
	protected Response onHandling(GameRoom data) {
		return null;
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		// TODO Auto-generated method stub
		
	}

}
