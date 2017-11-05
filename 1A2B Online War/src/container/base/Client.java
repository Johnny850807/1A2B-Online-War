package container;

import container.protocol.Protocol;

public interface Client extends Runnable{
	void respond(Protocol protocol);
	void disconnect() throws Exception;
}
