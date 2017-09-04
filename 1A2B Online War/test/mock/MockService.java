package mock;

import communication.Message;
import gamecore.entity.Entity;
import socket.UserService;

public class MockService implements UserService{

	private Object data;
	
	@Override
	public <T extends Entity> void respond(Message<T> message) {
		System.out.println(message);
		data = message.getData();
	}
	
	public Object getReceivedData() {
		return data;
	}

	@Override
	public void run() {}

}
