package container.eventhandler.handlers.games;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.core.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RoomStatus;
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
		try{
			GameRoom gameRoom = gameCore().getGameRoom(data.getGameRoomId());
			Game game = gameRoom.getGame();
			ClientPlayer clientPlayer = gameCore().getClientPlayer(data.getPlayerId());
			validateEntering(gameRoom, clientPlayer.getPlayer());
			game.enterGame(clientPlayer);
			gameRoom.pushLeisureTime();
			return success(data);
		}catch (NullPointerException e) {
			return error(404, e);
		}catch (IllegalStateException e) {
			return error(403, e);
		}
	}
	
	private void validateEntering(GameRoom room, Player player){
		if (!room.containsPlayer(player))
			throw new IllegalStateException("You are not in the room.");
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		//there is no need to broadcast the entered event.
	}

}
