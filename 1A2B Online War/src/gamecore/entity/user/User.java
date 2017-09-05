package gamecore.entity.user;

import communication.message.Message;
import gamecore.entity.Entity;

public interface User extends Entity {

	void sendMessage(Message<? extends Entity> message);

	String getName();

	void setName(String name);

}