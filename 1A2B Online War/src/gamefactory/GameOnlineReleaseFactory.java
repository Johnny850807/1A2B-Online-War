package gamefactory;

import java.io.InputStream;
import java.io.OutputStream;

import communication.CommandParser;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.GameCoreImp;
import gamecore.entity.Room;
import gamecore.entity.user.SocketProxyUser;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import socket.SocketService;
import socket.UserService;

public class GameOnlineReleaseFactory implements GameFactory{

	@Override
	public GameCore createGameCore() {
		return new GameCoreImp(this);
	}
	
	@Override
	public CommandParser createCommandParser(ProtocolFactory protocolFactory){
		return null;
	}

	@Override
	public User createUser(UserService userService, String name) {
		SocketProxyUser proxyUser = new SocketProxyUser(new UserImp(name));
		proxyUser.setService(userService);
		return proxyUser;
	}

	@Override
	public Room createRoom(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserService createService(InputStream input, OutputStream output) {
		return new SocketService(this, input, output);
	}

}
