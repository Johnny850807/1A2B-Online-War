package factory;

import command.CommandParser;
import gamecore.GameCore;
import gamecore.entity.Room;
import gamecore.entity.User;
import socket.UserService;

public interface GameFactory {
	GameCore createGameCore();
	User createUser(UserService userService, String name);
	CommandParser createCommandParser();
	Room createRoom(String name);
}
