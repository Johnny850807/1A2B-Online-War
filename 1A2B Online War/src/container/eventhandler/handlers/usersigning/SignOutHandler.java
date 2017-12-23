package container.eventhandler.handlers.usersigning;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Player;

public class SignOutHandler extends GsonEventHandler<Player, Player>{

	public SignOutHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
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
