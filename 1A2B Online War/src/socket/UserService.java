package socket;

import command.Message;
import gamecore.entity.Entity;

public interface UserService {
	void respond(Message<? extends Entity> message);
}
