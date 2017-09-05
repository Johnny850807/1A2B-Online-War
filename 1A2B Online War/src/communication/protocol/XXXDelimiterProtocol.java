package communication.protocol;

public class XXXDelimiterProtocol implements Protocol{
	static final String DELIMITER = "XXX";
	private String content;
	
	public XXXDelimiterProtocol(String content) {
		this.content = content;
	}
	
	//TODO parse protocol
	
	@Override
	public String getEvent() {
		return "";  //TODO stub
	}

	@Override
	public String getStatus() {
		return ""; //TODO stub
	}

	@Override
	public String getData() {
		return ""; //TODO stub
	}
	
	@Override
	public String toString() {
		return getEvent() + DELIMITER + getStatus() + DELIMITER + getData();
	}
	
	@Override
	public Protocol toProtocol(String event, String status, String data) {
		return new XXXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
	
}
