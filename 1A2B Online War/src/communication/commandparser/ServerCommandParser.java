package communication.commandparser;

import java.util.regex.Pattern;

import command.base.Command;
import command.server.GetServerInformationCommand;
import communication.commandparser.base.CommandParser;
import communication.message.Event;
import communication.message.Message;
import communication.message.MessageUtils;
import communication.message.Status;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.server.ServerInformation;
import userservice.UserService;

public class ServerCommandParser extends CommandParser{

	protected ServerCommandParser(GameCore gamecore, ProtocolFactory protocolFactory, UserService userService,
			CommandParser next) {
		super(gamecore, protocolFactory, userService, next);
	}

	@Override
	public Command parse(Protocol protocol) {
		String event = protocol.getEvent();
		
		if (Pattern.matches("(server)", event))
		{
			Message<String> message = MessageUtils.protocolToMessage(protocol);
			if (Pattern.matches(".*(information)", event)) //serverinformation
				return new GetServerInformationCommand(gameCore, userService, message);
				
		}
		
		return nextParse(protocol);
	}
	

}
