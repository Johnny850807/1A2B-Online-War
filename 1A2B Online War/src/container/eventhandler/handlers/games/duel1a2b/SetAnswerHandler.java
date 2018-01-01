package container.eventhandler.handlers.games.duel1a2b;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ContentModel;
import gamecore.model.games.ProcessInvalidException;
import gamecore.model.games.a1b2.Duel1A2BGame;
import gamecore.model.games.a1b2.NumberNotValidException;

/**
 * @author Johnny850807
 * Input: set answer model.
 * Output: (Client) set answer model.
 */
public class SetAnswerHandler extends GsonEventHandler<ContentModel, ContentModel>{
	public SetAnswerHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ContentModel> getDataClass() {
		return ContentModel.class;
	}

	@Override
	protected Response onHandling(ContentModel data) {
		try{
			GameRoom room = gameCore().getGameRoom(data.getRoomId());
			room.pushLeisureTime();
			Duel1A2BGame gameModel = (Duel1A2BGame) room.getGame();
			gameModel.commitPlayerAnswer(data.getPlayerId(), data.getContent());
			return success(data);
		}catch (NumberNotValidException|ProcessInvalidException err) {
			return error(400, err);
		}
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
