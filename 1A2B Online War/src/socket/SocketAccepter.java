package socket;

import gamefactory.GameFactory;

public class SocketAccepter implements Runnable{
	private GameFactory factory;

	
	public SocketAccepter(GameFactory factory){
		this.factory = factory;
	}


	@Override
	public void run() {
		// TODO 不停偵聽新的 socket 
		
	}
	
	
}
