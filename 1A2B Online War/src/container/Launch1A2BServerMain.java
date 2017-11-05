package container;

import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class Launch1A2BServerMain {

	public static void main(String[] args) {
		GameFactory factory = new GameOnlineReleaseFactory();
		new Thread(new SocketAccepter(factory)).start();
	}

}
