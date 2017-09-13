package gamefactory;

import communication.commandparser.CommandParser;
import communication.commandparser.CommandParserFactory;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.room.Room;
import gamecore.entity.user.User;
import userservice.ServiceIO;
import userservice.UserService;

public interface GameFactory {
	GameCore getGameCore();
	UserService createService(ServiceIO io);
	User createUser(UserService userService, String name);
	CommandParserFactory getCommandParserFactory();
	CommandParser createCommandParser(UserService userService);
	Room createRoom(String name);
	ProtocolFactory getProtocolFactory();
}
