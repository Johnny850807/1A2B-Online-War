package communication.commandparser.base;

import userservice.UserService;

public interface CommandParserFactory {
	CommandParser createCommandParser(UserService userService);
}
