package socket;

import communication.protocol.ProtocolFactory;
import gamefactory.GameFactory;

public class SocketAccepter implements Runnable{
	private GameFactory gameFactory;

	
	public SocketAccepter(GameFactory factory){
		this.gameFactory = factory;
	}


	@Override
	public void run() {
		// TODO ������ť�s�� socket 
		
	}
	
	
}
