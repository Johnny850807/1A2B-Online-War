package gamefactory;

import java.io.InputStream;
import java.io.OutputStream;

import org.hamcrest.Factory;

import communication.commandparser.CommandReleaseParserFactory;
import communication.commandparser.base.CommandParser;
import communication.commandparser.base.CommandParserFactory;
import communication.protocol.ProtocolFactory;
import communication.protocol.XOXOXDelimiterFactory;
import gamecore.GameCore;
import gamecore.GameCoreImp;
import gamecore.entity.room.Room;
import gamecore.entity.user.ServiceProxyUser;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import socket.SocketService;
import userservice.ServiceIO;
import userservice.UserService;
import utils.Singleton;

public class GameOnlineReleaseFactory implements GameFactory{
	private ProtocolFactory protocolFactory;
	private CommandParserFactory commandParserFactory;
	private GameCore gameCore;

	
	@Override
	@Singleton
	public GameCore getGameCore() {
		return gameCore == null ? gameCore = new GameCoreImp(this) : gameCore;
	}
	
	@Override
	public User createUser(UserService userService, String name) {
		ServiceProxyUser proxyUser = new ServiceProxyUser(new UserImp(name));
		proxyUser.setService(userService);
		return proxyUser;
	}

	@Override
	public Room createRoom(String name) {
		// TODO Room
		return null;
	}

	@Override
	public UserService createService(ServiceIO io) {
		return new SocketService(this, io);
	}

	@Override
	@Singleton
	public ProtocolFactory getProtocolFactory() {
		return protocolFactory == null ? protocolFactory = new XOXOXDelimiterFactory()
				: protocolFactory;
	}


	@Override
	@Singleton
	public CommandParserFactory getCommandParserFactory() {
		return commandParserFactory == null ? 
				commandParserFactory = new CommandReleaseParserFactory(getProtocolFactory(), getGameCore())
				: commandParserFactory;
	}

	@Override
	public CommandParser createCommandParser(UserService userService) {
		return getCommandParserFactory().createCommandParser(userService);
	}

}
