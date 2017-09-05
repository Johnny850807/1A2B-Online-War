package communication.protocol;

public class XXXDelimiterFactory implements ProtocolFactory{
	@Override
	public Protocol createProtocol(String content) {
		return new XXXDelimiterProtocol(content);
	}
}
