package gamecore.entity;

import command.Message;
import socket.UserService;

public class UserImp extends Entity implements User{
	private String name;
	
	public UserImp(String name) {
		this.name = name;
	}

	@Override
	public void sendMessage(Message<? extends Entity> message){
		System.out.println(message);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
}
