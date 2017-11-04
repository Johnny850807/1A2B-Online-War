package mock;

import communication.message.Event;
import communication.message.Message;
import container.UserService;
import gamecore.RequestStatus;
import gamecore.entity.Entity;
import gamecore.entity.user.UserImp;

public class MockService implements UserService{

	private Message<? extends Entity> data;

	@Override
	public void run() {
		// no operation
	}

	@Override
	public void respond(Message<? extends Entity> message) {
		System.out.println(message);
		data = message;
	}
	
	public Message<? extends Entity> getMessage() {
		return new Message<UserImp>(Event.signIn, RequestStatus.success, new UserImp("Test"));
	}

	@Override
	public void disconnect() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
