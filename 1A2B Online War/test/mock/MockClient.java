package mock;

import container.Client;
import container.protocol.Protocol;

public class MockClient implements Client{
	private Protocol response = null;

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


}
