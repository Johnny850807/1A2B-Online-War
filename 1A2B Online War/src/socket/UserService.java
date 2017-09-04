package socket;

import communication.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public interface UserService extends Runnable{
	<T extends Entity> void respond(Message<T> message);
}
