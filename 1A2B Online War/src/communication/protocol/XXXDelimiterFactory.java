package communication.protocol;

import static communication.protocol.XXXDelimiterProtocol.DELIMITER;

public class XXXDelimiterFactory implements ProtocolFactory{
	
	@Override
	public Protocol createProtocol(String content) {
		return new XXXDelimiterProtocol(content);
	}

	@Override
	public Protocol createProtocol(String event, String status, String data) {
		return new XXXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
}
