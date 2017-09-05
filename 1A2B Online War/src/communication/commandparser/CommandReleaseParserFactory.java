package communication.commandparser;

import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import socket.UserService;

public class CommandReleaseParserFactory implements CommandParserFactory {
	protected ProtocolFactory protocolFactory;
	protected GameCore gameCore;

	public CommandReleaseParserFactory(ProtocolFactory protocolFactory, GameCore gameCore) {
		this.protocolFactory = protocolFactory;
		this.gameCore = gameCore;
	}

	@Override
	public CommandParser createCommandParser(UserService userService) {
		CommandParser userParser = new UserCommandParser(gameCore, protocolFactory, userService, null);
		return userParser;
	}

}
