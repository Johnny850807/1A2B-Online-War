package container.base;

import container.protocol.Protocol;

public interface Client extends Runnable{
	void respond(Protocol protocol);
	String getId();
	String getAddress();
	void disconnect() throws Exception;
}
