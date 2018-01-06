package container.eventhandler.handlers.games.boss1a2b;

import container.core.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.model.ContentModel;
import gamecore.model.games.ProcessInvalidException;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.core.NumberNotValidException;
import gamecore.model.games.a1b2.duel.imp.Duel1A2BGame;

/**
 * @author Waterball
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
			IBoss1A2BGame game = (IBoss1A2BGame) room.getGame();
			game.setPlayerAnswer(data.getPlayerId(), data.getContent());
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
