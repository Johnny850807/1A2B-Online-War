package gamefactory;

import container.ServiceIO;
import container.UserService;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;

public interface GameFactory {
	GameCore getGameCore();
	UserService createService(ServiceIO io);
	ProtocolFactory getProtocolFactory();
}
