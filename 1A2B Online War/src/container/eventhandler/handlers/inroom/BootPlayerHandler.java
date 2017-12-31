package container.eventhandler.handlers.inroom;

import container.Constants.Events.InRoom;
import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RequestStatus;

/**
 * @author Johnny850807
 * Input: the PlayerRoomIdModel.
 * Output: invoke the leave room handler.
 */
public class BootPlayerHandler extends GsonEventHandler<PlayerRoomIdModel, PlayerRoomModel>{
	private ClientPlayer bootedPlayer;
	public BootPlayerHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<PlayerRoomIdModel> getDataClass() {
		return PlayerRoomIdModel.class;
	}

	@Override
	protected Response onHandling(PlayerRoomIdModel data) {
		GameRoom room = gameCore().getGameRoom(data.getGameRoomId());
		bootedPlayer = gameCore().getClientPlayer(data.getPlayerId());
		
		//the leave room handler will emit the leave event to the signed user and the room
		//so everyone could see the player booted from the room.
		Protocol protocol = protocolFactory().createProtocol(InRoom.LEAVE_ROOM, RequestStatus.request.toString(),
				gson.toJson(data));
		new LeaveRoomHandler(client(), protocol, gameCore(), protocolFactory()).handle();
		
		
		//then emit the booted event to the booted player, so he will know he got booted
		return success(new PlayerRoomModel(bootedPlayer.getPlayer(), room));
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		bootedPlayer.broadcast(responseProtocol);
	}

}
