package socket;

import java.io.InputStream;
import java.io.OutputStream;

import communication.message.Message;
import gamecore.entity.Entity;
import gamefactory.GameFactory;

public class SocketService implements UserService{
	private GameFactory factory;

	public SocketService(GameFactory factory, InputStream input, OutputStream output) {
		this.factory = factory;
	}


	@Override
	public void run() {
		// TODO ��ťI/O
		
	}
	
	@Override
	public void respond(Message<? extends Entity> message) {
		// TODO �^���T���� client
	}


	@Override
	public void disconnect() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
