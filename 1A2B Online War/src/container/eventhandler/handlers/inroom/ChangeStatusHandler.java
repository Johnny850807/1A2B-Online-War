package container.eventhandler.handlers.inroom;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ChangeStatusModel;
import gamecore.model.ClientPlayer;

/**
 * @author Johnny850807
 * Input: the change status model.
 * Output: (Room) the change status model.
 */
public class ChangeStatusHandler extends GsonEventHandler<ChangeStatusModel, ChangeStatusModel>{
	private String roomId;
	public ChangeStatusHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ChangeStatusModel> getDataClass() {
		return ChangeStatusModel.class;
	}

	@Override
	protected Response onHandling(ChangeStatusModel data) {
		roomId = data.getRoomId();
		GameRoom gameRoom = gameCore().getGameRoom(roomId);
		ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
		
		if (gameRoom.ifPlayerInStatusList(clientPlayer.getPlayer()))
		{
			gameRoom.changePlayerStatus(clientPlayer.getPlayer(), data.isPrepare());
			return success(data);
		}
		else
			return error(201, new IllegalArgumentException("The player is not in the status list. (or is he a host?)"));
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastRoom(roomId, responseProtocol);
	}

}
