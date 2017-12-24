package container.eventhandler.handlers.usersigning;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ClientStatus;

public class SignInHandler extends GsonEventHandler<Player, Player>{

	public SignInHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<Player> getDataClass() {
		return Player.class;
	}

	@Override
	protected Response onHandling(Player player) {
		String name = player.getName();
		if (name == null || name.length() == 0 || name.length() > 6)
			return error(100, new IllegalArgumentException("The user name's length cannot be out of the range (1~6)."));
		player.setId(client().getId()); // set the id of the player's corresponding to the socket's
		player.setUserStatus(ClientStatus.signedIn);
		gameCore().addBindedClientPlayer(client(), player);
		return success(player);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().broadcast(responseProtocol);
	}

}
