package communication.protocol;

public interface ProtocolFactory {
	Protocol createProtocol(String content);
	Protocol createProtocol(String event, String status, String data);
}
