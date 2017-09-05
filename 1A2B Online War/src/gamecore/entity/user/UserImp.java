package gamecore.entity.user;

import communication.message.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;
import socket.UserService;

public class UserImp extends BaseEntity implements User{
	private String name;
	
	public UserImp() {}
	
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
