package container.protocol;

public interface Protocol {
	String getEvent();
	String getStatus();
	String getData();
	String toString();
	Protocol toProtocol(String event, String status, String data);
}
