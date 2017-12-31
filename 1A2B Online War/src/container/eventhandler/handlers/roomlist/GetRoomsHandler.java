package container.eventhandler.handlers.roomlist;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.RoomStatus;

/**
 * @author Johnny850807
 * Input: none.
 * Output: (client) the room list.
 */
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
		return success(gameCore().getGameRooms().stream()
				.filter(r -> r.getRoomStatus() == RoomStatus.waiting).collect(Collectors.toList()));
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
