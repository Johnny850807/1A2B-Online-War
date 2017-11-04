package gamefactory;

import container.ServiceIO;
import container.eventhandler.GameEventHandlerFactory;
import container.Client;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

public interface GameFactory {
	GameCore getGameCore();
	Client createService(ServiceIO io);
	ProtocolFactory getProtocolFactory();
	GameEventHandlerFactory getGameEventHandlerFactory();
}
