package socket;

import factory.GameFactory;

public class SocketAccepter implements Runnable{
	private GameFactory factory;

	
	public SocketAccepter(GameFactory factory){
		this.factory = factory;
	}


	@Override
	public void run() {
		// TODO ��ť�s�� socket 
		
	}
	
	
}
