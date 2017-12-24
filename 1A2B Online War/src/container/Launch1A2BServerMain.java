package container;

import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class Launch1A2BServerMain {

	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile","configuration.xml");
		GameFactory factory = new GameOnlineReleaseFactory();
		new Thread(new SocketAccepter(factory)).start();
	}

}
