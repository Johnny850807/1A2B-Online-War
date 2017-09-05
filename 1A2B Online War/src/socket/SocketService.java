package socket;

import java.io.InputStream;
import java.io.OutputStream;

import communication.message.Message;
import communication.protocol.ProtocolFactory;
import gamecore.entity.Entity;
import gamefactory.GameFactory;

public class SocketService implements UserService{
	private GameFactory gameFactory;
	private ProtocolFactory protocolFactory;

	public SocketService(GameFactory factory, InputStream input, OutputStream output) {
		this.gameFactory = factory;
		this.protocolFactory = gameFactory.createProtocolFactory();
		
		//TODO handle input and output stream
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
		// TODO ���_�s��
		
	}

}
