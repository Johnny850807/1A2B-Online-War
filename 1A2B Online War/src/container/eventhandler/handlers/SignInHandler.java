package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.UserStatus;

public class SignInHandler extends GsonEventHandler<Player, Player>{

	public SignInHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<Player> getDataClass() {
		return Player.class;
	}

	@Override
	protected Response onHandling(Player data) {
		String name = data.getName();
		if (name == null || name.length() == 0 || name.length() > 6)
			return error(100, new IllegalArgumentException("The user name's length cannot be out of the range (1~6)."));
		data.initId();
		data.setUserStatus(UserStatus.SignedIn);
		gameCore().getClientsMap().put(data, client());
		return success(data);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
