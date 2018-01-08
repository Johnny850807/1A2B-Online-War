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
 * Input: guess model.
 * Output: (Client) guess model.
 */
public class AttackHandler extends GsonEventHandler<ContentModel, ContentModel>{

	public AttackHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ContentModel> getDataClass() {
		return ContentModel.class;
	}

	@Override
	protected Response onHandling(ContentModel attackModel) {
		try{
			GameRoom gameRoom = gameCore().getGameRoom(attackModel.getRoomId());
			IBoss1A2BGame gameModel = (IBoss1A2BGame) gameRoom.getGame();
			gameModel.attack(attackModel.getPlayerId(), attackModel.getContent());
			return success(attackModel);
		}catch (NumberNotValidException|ProcessInvalidException e) {
			return error(400, e);
		}
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
