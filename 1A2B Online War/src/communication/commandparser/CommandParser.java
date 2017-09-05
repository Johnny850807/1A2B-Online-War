package communication.commandparser;

import command.Command;
import command.NullCommand;
import communication.message.Message;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import socket.UserService;

public abstract class CommandParser {
	protected ProtocolFactory protocolFactory;
	protected GameCore gameCore;
	protected UserService userService;
	protected CommandParser next;
	
	CommandParser(GameCore gamecore, 
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
