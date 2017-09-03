package gamecore.entity;

import command.Message;

public interface User {

	void sendMessage(Message<? extends Entity> message);

	String getName();

	void setName(String name);

}