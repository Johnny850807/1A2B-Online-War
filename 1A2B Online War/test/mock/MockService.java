package mock;

import command.Message;
import gamecore.entity.Entity;
import socket.UserService;

public class MockService implements UserService{
	private boolean hasBeenResponded = false;
	@Override
	public void respond(Message<? extends Entity> message) {
		System.out.println(message);
		hasBeenResponded = true;
	}
	
	public boolean hasBeenResponded() {
		return hasBeenResponded;
	}

}
