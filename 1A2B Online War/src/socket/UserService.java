package socket;

import communication.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public interface UserService extends Runnable{
	void respond(Message<? super Entity> message);
}
