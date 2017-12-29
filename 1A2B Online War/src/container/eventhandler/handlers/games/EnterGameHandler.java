package container.eventhandler.handlers.games;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.games.Game;

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
		GameRoom gameRoom = gameCore().getGameRoom(data.getGameRoomId());
		Game game = gameRoom.getGame();
		game.enterGame(gameCore().getClientPlayer(data.getPlayerId()));
		return success(data);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		//there is no need to broadcast the entered event.
	}

}
