package gamecore.entity;

import communication.Message;

public interface User extends Entity {

	void sendMessage(Message<? super Entity> message);

	String getName();

	void setName(String name);

}