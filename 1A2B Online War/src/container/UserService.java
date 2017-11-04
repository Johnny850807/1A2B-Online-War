package container;

import container.protocol.Protocol;

public interface UserService extends Runnable{
	void respond(Protocol protocol);
	void disconnect() throws Exception;
}
