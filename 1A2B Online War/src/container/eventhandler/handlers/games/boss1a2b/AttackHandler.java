package container.eventhandler.handlers.games.boss1a2b;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.model.ContentModel;

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
		return null;
	}

	@Override
	protected Response onHandling(ContentModel data) {
		return null;
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		
	}

}
