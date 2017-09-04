package socket;

import communication.Message;
import communication.RequestParser;
import factory.GameFactory;
import gamecore.entity.Entity;

public class SocketService implements UserService{
	private GameFactory factory;
	private RequestParser requestParser;
	

	public SocketService(GameFactory factory) {
		this.factory = factory;
		requestParser = factory.createRequestParser();
	}


	@Override
	public void run() {
		// TODO °»Å¥I/O
		
	}


	@Override
	public <T extends Entity> void respond(Message<T> message) {
		// TODO Auto-generated method stub
		
	}

}
