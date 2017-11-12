package gamefactory;

import container.base.Client;
import container.base.IO;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

public interface GameFactory {
	GameCore getGameCore();
	Client createService(IO io, String address);
	ProtocolFactory getProtocolFactory();
	GameEventHandlerFactory getGameEventHandlerFactory();
}
