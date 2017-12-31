package container.waterbot;

import container.protocol.Protocol;

public interface Brain {
	void react(WaterBot waterBot, Protocol protocol);
}
