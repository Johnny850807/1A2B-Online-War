package gamefactory;

import java.io.InputStream;
import java.io.OutputStream;

import communication.commandparser.CommandParser;
import communication.commandparser.CommandParserFactory;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.room.Room;
import gamecore.entity.user.User;
import socket.UserService;

public interface GameFactory {
	GameCore createGameCore();
	UserService createService(InputStream input, OutputStream output);
	User createUser(UserService userService, String name);
	CommandParserFactory createCommandParserFactory();
	CommandParser createCommandParser(UserService userService);
	Room createRoom(String name);
	ProtocolFactory createProtocolFactory();
}
