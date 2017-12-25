package container.eventhandler.handlers.roomlist;

import org.omg.CORBA.PRIVATE_MEMBER;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;

/**
 * @author Johnny850807
 * Input: the joining room.
 * Output: (InRoom / RoomList) the joined model.
 */
public class JoinRoomHandler extends GsonEventHandler<PlayerRoomIdModel, PlayerRoomModel>{
	private String roomId;
	public JoinRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<PlayerRoomIdModel> getDataClass() {
		return PlayerRoomIdModel.class;
	}

	@Override
	protected Response onHandling(PlayerRoomIdModel data) {
		try{
			roomId = data.getGameRoomId();
			GameRoom room = gameCore().getGameRoom(roomId);
			ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
			room.addPlayer(clientPlayer.getPlayer());
			return success(new PlayerRoomModel(clientPlayer.getPlayer(), room));
		}catch (Exception e) {
			return error(404, e);
		}
		
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastRoom(roomId, responseProtocol);
		gameCore().broadcastClientPlayers(ClientStatus.signedIn, responseProtocol);
	}

}
