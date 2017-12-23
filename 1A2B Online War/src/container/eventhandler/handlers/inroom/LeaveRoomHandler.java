package container.eventhandler.handlers.inroom;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.ClientPlayer;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;

/**
 * @author Johnny850807
 * Input: the id model.
 * Output: (RoomList, InRoom) the model.
 */
public class LeaveRoomHandler extends GsonEventHandler<PlayerRoomIdModel, PlayerRoomModel>{
	private GameRoom gameRoom;
	public LeaveRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<PlayerRoomIdModel> getDataClass() {
		return PlayerRoomIdModel.class;
	}

	@Override
	protected Response onHandling(PlayerRoomIdModel data) {
		gameRoom = gameCore().getGameRoom(data.getGameRoomId());
		int beforeAmount = gameRoom.getPlayerAmount();
		ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
		gameCore().removePlayerFromRoom(clientPlayer.getPlayer(), gameRoom);
		int afterPlayerAmount =  gameRoom.getPlayerAmount();
		assert afterPlayerAmount == beforeAmount - 1 : "Remove failed, before player amount: " + beforeAmount + ", after: " + afterPlayerAmount;
		return success(new PlayerRoomModel(clientPlayer.getPlayer(), gameRoom));
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		// the gamecore has handled it.
	}


}
