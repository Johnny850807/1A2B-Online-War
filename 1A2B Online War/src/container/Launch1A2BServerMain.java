package container;

import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class Main {

	public static void main(String[] args) {
		GameFactory factory = new GameOnlineReleaseFactory();
		new Thread(new SocketAccepter(factory)).start();
	}

}
