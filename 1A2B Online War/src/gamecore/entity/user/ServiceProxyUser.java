package gamecore.entity.user;

import com.google.gson.annotations.Expose;

import communication.message.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;
import userservice.UserService;

public class ServiceProxyUser implements User{
	private UserService service;
	private User user;
	
	public ServiceProxyUser(User user) {
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
	public int compareTo(Entity o) {
		return user.compareTo(o);
	}

}
