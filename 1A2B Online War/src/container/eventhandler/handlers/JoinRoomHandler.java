package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.ClientPlayer;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.JoinRoomModel;

public class JoinRoomHandler extends GsonEventHandler<JoinRoomModel, GameRoom>{

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
			return success(room);
		}catch (Exception e) {
			return error(404, e);
		}
		
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
