package module;

import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class FactoryModule {
	private static GameFactory factory = new GameOnlineReleaseFactory();
	
	public static GameFactory getGameFactory(){
		return factory;
	}
}
