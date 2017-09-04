package factory;

import communication.CommandParser;
import communication.RequestParser;
import communication.RequestParserImp;
import gamecore.GameCore;
import gamecore.GameCoreImp;
import gamecore.entity.Room;
import gamecore.entity.SocketProxyUser;
import gamecore.entity.User;
import gamecore.entity.UserImp;
import socket.SocketService;
import socket.UserService;

public class GameOnlineReleaseFactory implements GameFactory{

	@Override
	public GameCore createGameCore() {
		return new GameCoreImp(this);
	}
	
	@Override
	public CommandParser createCommandParser(){
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
	public RequestParser createRequestParser() {
		return new RequestParserImp();
	}

	@Override
	public UserService createService() {
		return new SocketService(this);
	}

}
