package container.protocol;

import static container.protocol.XOXOXDelimiterProtocol.DELIMITER;

public class XOXOXDelimiterFactory implements ProtocolFactory{
	
	@Override
	public Protocol createProtocol(String content) {
		return new XOXOXDelimiterProtocol(content);
	}

	@Override
	public Protocol createProtocol(String event, String status, String data) {
		data = data == null ? "{}" : data; // prevent the null text
		return new XOXOXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
}
