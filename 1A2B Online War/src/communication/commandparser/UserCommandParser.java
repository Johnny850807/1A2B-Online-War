package communication.commandparser;

import java.util.regex.Pattern;

import com.google.gson.Gson;

import command.Command;
import command.user.SignInCommand;
import communication.message.Event;
import communication.message.Message;
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
		if (Pattern.matches("^((?i)sign).*", event))
		{
			Message<User> message = protocolToMessage(protocol);
			
			if (Pattern.matches(".*((?i)in)$", event))
				return new SignInCommand(gameCore, userService, message);
			
			//TODO signOut
		}
		
		return nextParse(protocol);
	}
	
	private Message<User> protocolToMessage(Protocol protocol){
		String data = protocol.getData();
		Event event = Event.valueOf(protocol.getEvent());
		Status status = Status.valueOf(protocol.getStatus());
		User user = new Gson().fromJson(data, UserImp.class);
		return new Message<>(event, status, user);
	}

}
