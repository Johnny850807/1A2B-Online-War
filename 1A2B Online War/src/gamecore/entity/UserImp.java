package gamecore.entity;

import communication.Message;
import socket.UserService;

public class UserImp extends BaseEntity implements User{
	private String name;
	
	public UserImp() {}
	
	public UserImp(String name) {
		this.name = name;
	}

	@Override
	public void sendMessage(Message<? super Entity> message){
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
