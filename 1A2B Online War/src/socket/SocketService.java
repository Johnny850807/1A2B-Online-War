package socket;

import command.Message;
import gamecore.entity.Entity;

public class SocketService implements UserService{

	@Override
	public void respond(Message<? extends Entity> message) {
		// TODO 回傳訊息給client socket
	}

}
