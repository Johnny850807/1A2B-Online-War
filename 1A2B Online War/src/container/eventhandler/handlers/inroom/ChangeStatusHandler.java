package container.eventhandler.handlers.inroom;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.ClientPlayer;
import gamecore.model.RoomStatus;

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
		try{
			roomId = data.getRoomId();
			GameRoom gameRoom = gameCore().getGameRoom(roomId);
			ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
			validateChangingStatus(gameRoom, clientPlayer.getPlayer());
			gameRoom.changePlayerStatus(clientPlayer.getPlayer(), data.isPrepare());
			return success(data);
		}catch (NullPointerException e) {
			return error(404, e);
		}catch (IllegalArgumentException e) {
			return error(400, e);
		}catch (IllegalStateException e) {
			return error(403, e);
		}
	}
	
	private void validateChangingStatus(GameRoom gameRoom, Player player){
		if (!gameRoom.ifPlayerInStatusList(player))
			throw new IllegalArgumentException("The player is not in the status list. (or is he a host?)");
		if (gameRoom.getRoomStatus() == RoomStatus.gamestarted)
			throw new IllegalStateException("The gameroom has been started.");
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastRoom(roomId, responseProtocol);
	}

}
