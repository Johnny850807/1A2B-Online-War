package gamecore.entity;

import communication.Message;
import socket.UserService;

public class SocketProxyUser implements User{
	private UserService service;
	private User user;
	
	public SocketProxyUser(User user) {
		this.user = user;
	}
	
	public void setService(UserService service) {
		this.service = service;
	}
	
	public UserService getService() {
		return service;
	}

	@Override
	public void sendMessage(Message<? extends Entity> message) {
		service.respond(message);
		user.sendMessage(message);
	}

	@Override
	public String getName() {
		return user.getName();
	}

	@Override
	public void setName(String name) {
		user.setName(name);
	}

	@Override
	public void setId(String id) {
		user.setId(id);
	}

	@Override
	public String getId() {
		return user.getId();
	}

	@Override
	public int compareTo(BaseEntity o) {
		return user.compareTo(o);
	}

}
