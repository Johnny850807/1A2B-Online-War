package gamefactory;

import java.io.InputStream;
import java.io.OutputStream;

import org.hamcrest.Factory;

import communication.commandparser.CommandParser;
import communication.commandparser.CommandParserFactory;
import communication.commandparser.CommandReleaseParserFactory;
import communication.protocol.ProtocolFactory;
import communication.protocol.XOXOXDelimiterFactory;
import gamecore.GameCore;
import gamecore.GameCoreImp;
import gamecore.entity.room.Room;
import gamecore.entity.user.SocketProxyUser;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import socket.SocketService;
import socket.UserService;
import utils.Singleton;

public class GameOnlineReleaseFactory implements GameFactory{
	private ProtocolFactory protocolFactory;
	private CommandParserFactory commandParserFactory;
	private GameCore gameCore;

	
	@Override
	@Singleton
	public GameCore createGameCore() {
		return gameCore == null ? gameCore = new GameCoreImp(this) : gameCore;
	}
	
	@Override
	public User createUser(UserService userService, String name) {
		SocketProxyUser proxyUser = new SocketProxyUser(new UserImp(name));
		proxyUser.setService(userService);
		return proxyUser;
	}

	@Override
	public Room createRoom(String name) {
		// TODO Room
		return null;
	}

	@Override
	public UserService createService(InputStream input, OutputStream output) {
		return new SocketService(this, input, output);
	}

	@Override
	@Singleton
	public ProtocolFactory createProtocolFactory() {
		return protocolFactory == null ? protocolFactory = new XOXOXDelimiterFactory()
				: protocolFactory;
	}


	@Override
	@Singleton
	public CommandParserFactory createCommandParserFactory() {
		return commandParserFactory == null ? 
				commandParserFactory = new CommandReleaseParserFactory(createProtocolFactory(), createGameCore())
				: commandParserFactory;
	}

	@Override
	public CommandParser createCommandParser(UserService userService) {
		return createCommandParserFactory().createCommandParser(userService);
	}

}
