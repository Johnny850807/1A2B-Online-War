package communication;

import command.Command;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;

public abstract class CommandParser {
	protected CommandParser next;
	
	public CommandParser(ProtocolFactory protocolFactory, CommandParser next){
		this.next = next;
	}
	
	public abstract Command parse(Protocol protocol);
}
