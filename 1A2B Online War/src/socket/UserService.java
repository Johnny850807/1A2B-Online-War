package socket;

import communication.message.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public interface UserService extends Runnable{
	void respond(Message<? extends Entity> message);
	void disconnect() throws Exception;
}
