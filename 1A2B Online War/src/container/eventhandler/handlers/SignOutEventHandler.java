package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Player;

public class SignOutEventHandler extends GsonEventHandler<Player, Player>{

	public SignOutEventHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<Player> getDataClass() {
		return Player.class;
	}

	@Override
	protected Response onHandling(Player player) {
		if (player.getId() == null)
			return error(404, new IllegalArgumentException("Id should not be null."));
		gameCore().removeClientPlayer(player.getId());
		return success(player);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
