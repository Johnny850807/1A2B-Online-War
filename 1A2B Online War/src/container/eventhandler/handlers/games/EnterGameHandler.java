package container.eventhandler.handlers.games;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.model.PlayerRoomIdModel;

/**
 * @author Waterball
 * Input: PlayerRoomIdModel model.
 * Output: (Client) PlayerRoomIdModel model.
 */
public class EnterGameHandler extends GsonEventHandler<PlayerRoomIdModel, PlayerRoomIdModel>{

	public EnterGameHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<PlayerRoomIdModel> getDataClass() {
		return PlayerRoomIdModel.class;
	}

	@Override
	protected Response onHandling(PlayerRoomIdModel data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		// TODO Auto-generated method stub
		
	}

}
