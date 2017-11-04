package container.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XOXOXDelimiterProtocol implements Protocol{
	static final String DELIMITER = "XOXOX";
	private String content;
	private String event;
	private String status;
	private String data;
	
	public XOXOXDelimiterProtocol(String content) {
		this.content = content;
		String[] snippets = content.split(DELIMITER);
		event = snippets[0];
		status = snippets[1];
		data = snippets[2];
	}
	
	//TODO parse protocol
	
	@Override
	public String getEvent() {
		return event;
	}

	@Override
	public String getStatus() {
		return status; //TODO stub
	}

	@Override
	public String getData() {
		return data; //TODO stub
	}
	
	@Override
	public String toString() {
		return getEvent() + DELIMITER + getStatus() + DELIMITER + getData();
	}
	
	@Override
	public Protocol toProtocol(String event, String status, String data) {
		return new XOXOXDelimiterProtocol(event + DELIMITER + status + DELIMITER + data);
	}
	
}
