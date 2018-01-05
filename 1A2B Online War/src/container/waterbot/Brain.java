package container.waterbot;

import container.core.Client;
import container.protocol.Protocol;

public interface Brain {
	void react(WaterBot waterBot, Protocol protocol, Client client);
}
