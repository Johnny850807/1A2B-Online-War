package container.base;

import container.protocol.Protocol;

public interface Client extends Runnable{
	void broadcast(Protocol protocol);
	String getId();
	String getAddress();
	void disconnect() throws Exception;
}
