package communication.commandparser.base;

import command.base.Command;
import command.base.NullCommand;
import communication.message.Message;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import userservice.UserService;

public abstract class CommandParser {
	protected ProtocolFactory protocolFactory;
	protected GameCore gameCore;
	protected UserService userService;
	protected CommandParser next;
	
	protected CommandParser(GameCore gamecore, 
			ProtocolFactory protocolFactory, 
			UserService userService,
			CommandParser next){
		this.next = next;
		this.protocolFactory = protocolFactory;
		this.userService = userService;
		this.gameCore = gamecore;
	}
	
	public abstract Command parse(Protocol protocol);
	
	public Command nextParse(Protocol protocol) {
		if (next != null)
			return next.parse(protocol);
		return new NullCommand();
	}
	
}
