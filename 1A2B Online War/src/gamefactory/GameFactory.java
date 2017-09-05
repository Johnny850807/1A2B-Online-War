package gamefactory;

import java.io.InputStream;
import java.io.OutputStream;

import communication.CommandParser;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Room;
import gamecore.entity.user.User;
import socket.UserService;

public interface GameFactory {
	GameCore createGameCore();
	UserService createService(InputStream input, OutputStream output);
	User createUser(UserService userService, String name);
	CommandParser createCommandParser(ProtocolFactory protocolFactory);
	Room createRoom(String name);
}
