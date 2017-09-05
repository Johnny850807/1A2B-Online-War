package communication.protocol;

public class XXXDelimiterProtocol implements Protocol{
	private static final String DELIMITER = "XXX";
	private String content;
	
	public XXXDelimiterProtocol(String content) {
		this.content = content;
	}
	
	//TODO parse protocol
	
	@Override
	public String getEvent() {
		return "";
	}

	@Override
	public String getStatus() {
		return "";
	}

	@Override
	public String getData() {
		return "";
	}
	
	@Override
	public String toString() {
		return getEvent() + DELIMITER + getStatus() + DELIMITER + getData();
	}
	
	public Protocol toProtocol(String event, String status, String data) {
		return new XXXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
	
}
