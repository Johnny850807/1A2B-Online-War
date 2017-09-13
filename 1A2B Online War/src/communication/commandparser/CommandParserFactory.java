package communication.commandparser;

import userservice.UserService;

public interface CommandParserFactory {
	CommandParser createCommandParser(UserService userService);
}
