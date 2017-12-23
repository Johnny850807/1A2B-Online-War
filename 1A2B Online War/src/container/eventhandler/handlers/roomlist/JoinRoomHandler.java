package container.eventhandler.handlers.roomlist;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.ClientPlayer;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.JoinRoomModel;

/**
 * @author Johnny850807
 * Input: the joining room.
 * Output: (InRoom / RoomList) the joined player.
 */
public class JoinRoomHandler extends GsonEventHandler<JoinRoomModel, Player>{

	public JoinRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<JoinRoomModel> getDataClass() {
		return JoinRoomModel.class;
	}

	@Override
	protected Response onHandling(JoinRoomModel data) {
		try{
			GameRoom room = gameCore().getGameRoom(data.getGameRoomId());
			ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
			room.addPlayer(clientPlayer.getPlayer());
			return success(clientPlayer.getPlayer());
		}catch (Exception e) {
			return error(404, e);
		}
		
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
