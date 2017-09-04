package factory;

import communication.CommandParser;
import communication.RequestParser;
import gamecore.GameCore;
import gamecore.entity.Room;
import gamecore.entity.User;
import socket.UserService;

public interface GameFactory {
	GameCore createGameCore();
	UserService createService();
	User createUser(UserService userService, String name);
	CommandParser createCommandParser();
	Room createRoom(String name);
	RequestParser createRequestParser();
}
