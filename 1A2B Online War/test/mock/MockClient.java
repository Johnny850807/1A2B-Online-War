package mock;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.Entity;

public class MockClient extends Entity implements Client{
	private Protocol response = null;

	public MockClient() {
		initId();
	}
	
	@Override
	public void run() {}

	public Protocol getResponse() {
		return response;
	}

	@Override
	public void disconnect() throws Exception {}



	@Override
	public void respond(Protocol protocol) {
		this.response = protocol;
	}

	@Override
	public String getAddress() {
		return "test client";
	}


}
