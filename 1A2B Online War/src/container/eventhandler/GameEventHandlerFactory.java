package container.eventhandler;

import container.Client;
import container.protocol.Protocol;

public interface GameEventHandlerFactory {
	EventHandler createGameEventHandler(Client client, Protocol protocol);
}
