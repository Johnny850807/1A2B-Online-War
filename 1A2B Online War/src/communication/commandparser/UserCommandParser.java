package communication.commandparser;

import java.util.regex.Pattern;

import com.google.gson.Gson;

import command.base.Command;
import command.user.SignInCommand;
import communication.commandparser.base.CommandParser;
import communication.message.Event;
import communication.message.Message;
import communication.message.MessageUtils;
import communication.message.Status;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import userservice.UserService;

public class UserCommandParser extends CommandParser{

	UserCommandParser(GameCore gamecore, ProtocolFactory protocolFactory, UserService userService, CommandParser next) {
		super(gamecore, protocolFactory, userService, next);
	}

	@Override
	public Command parse(Protocol protocol) {
		String event = protocol.getEvent();
		if (Pattern.matches("^(sign).*", event))
		{
			Message<User> message = MessageUtils.protocolToMessage(protocol, UserImp.class);
			
			if (Pattern.matches(".*(In)$", event))
				return new SignInCommand(gameCore, userService, message);
			
			//TODO signOut
		}
		
		return nextParse(protocol);
	}
	
}
