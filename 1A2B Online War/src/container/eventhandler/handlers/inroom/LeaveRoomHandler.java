package container.eventhandler.handlers.inroom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;

/**
 * @author Johnny850807
 * Input: the id model.
 * Output: (RoomList, InRoom) the model.
 */
public class LeaveRoomHandler extends GsonEventHandler<PlayerRoomIdModel, PlayerRoomModel>{
	private static Logger log = LogManager.getLogger(LeaveRoomHandler.class);
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
		try{
			gameRoom = gameCore().getGameRoom(data.getGameRoomId());
			ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
			gameCore().removePlayerFromRoomAndBroadcast(clientPlayer.getPlayer(), gameRoom);
			clientPlayer.pushLeisureTime();
			return success(new PlayerRoomModel(clientPlayer.getPlayer(), gameRoom));
		}catch (NullPointerException e) {
			return error(404, e);
		}
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		// the gamecore has handled it.
	}


}
