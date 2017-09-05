package communication.commandparser;

import socket.UserService;

public interface CommandParserFactory {
	CommandParser createCommandParser(UserService userService);
}
