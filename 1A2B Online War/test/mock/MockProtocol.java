package mock;

import container.protocol.Protocol;

public class MockProtocol implements Protocol{
	private String event;
	private String status;
	private String data;
	
	public MockProtocol(String event, String status, String data) {
		this.event = event;
		this.status = status;
		this.data = data;
	}

	@Override
	public String getEvent() {
		return event;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getData() {
		return data;
	}

	@Override
	public Protocol toProtocol(String event, String status, String data) {
		throw new IllegalStateException("Don't use toProtocol(), on the mock protocol.");
	}

}
