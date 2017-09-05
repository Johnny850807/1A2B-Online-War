package communication.protocol;

public interface Protocol {
	String getEvent();
	String getStatus();
	String getData();
	Protocol toProtocol(String event, String status, String data);
	String toString();
}
