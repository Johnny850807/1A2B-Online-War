package container.eventhandler.handlers;

import container.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.RequestStatus;
import gamecore.UserStatus;
import gamecore.entity.User;

public class SignInHandler extends GsonEventHandler<User>{

	public SignInHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<User> getDataClass() {
		return User.class;
	}

	@Override
	protected Response onHandling(User data) {
		String name = data.getName();
		if (name.length() == 0 || name.length() > 6)
			return error(100, new IllegalArgumentException("The user name's length cannot be out of the range (1~6)."));
		data.initId();
		data.setUserStatus(UserStatus.SignedIn);
		gameCore().getUserContainer().add(data);
		return success(data);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		client().respond(responseProtocol);
	}

}
