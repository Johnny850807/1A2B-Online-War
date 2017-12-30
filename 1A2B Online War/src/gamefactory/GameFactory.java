package gamefactory;

import container.base.Client;
import container.base.ClientBinder;
import container.base.IO;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

/**
 * The factory which implements a abstraction factory grouping a set of
 * dependencies used for holding a system.
 */
public interface GameFactory {
	GameCore getGameCore();
	Client createService(IO io, String address);
	
	/**
	 * @return singleton of protocol factory.
	 */
	ProtocolFactory getProtocolFactory();
	
	/**
	 * @return singleton of game event handler factory.
	 */
	GameEventHandlerFactory getGameEventHandlerFactory();
	
}