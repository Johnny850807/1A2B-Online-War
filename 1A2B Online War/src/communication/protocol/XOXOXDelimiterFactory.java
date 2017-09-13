package communication.protocol;

import static communication.protocol.XOXOXDelimiterProtocol.DELIMITER;

public class XOXOXDelimiterFactory implements ProtocolFactory{
	
	@Override
	public Protocol createProtocol(String content) {
		return new XOXOXDelimiterProtocol(content);
	}

	@Override
	public Protocol createProtocol(String event, String status, String data) {
		return new XOXOXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
}
