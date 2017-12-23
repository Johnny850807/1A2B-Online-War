package gamefactory;

import container.SocketClient;
import container.base.Client;
import container.base.IO;
import container.eventhandler.ConfigBasedGameEventHandlerFactory;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.ProtocolFactory;
import container.protocol.XOXOXDelimiterFactory;
import gamecore.GameCore;
import gamecore.ReleaseGameCore;

public class GameOnlineReleaseFactory implements GameFactory{
	private GameEventHandlerFactory gameEventHandlerFactory;
	private ProtocolFactory protocolFactory;
	private GameCore gameCore;

	
	@Override
	public GameCore getGameCore() {
		return gameCore == null ? gameCore = new ReleaseGameCore(this) : gameCore;
	}
	
	@Override
	public Client createService(IO io, String address) {
		return new SocketClient(this, io, address);
	}

	@Override
	public ProtocolFactory getProtocolFactory() {
		return protocolFactory == null ? protocolFactory = new XOXOXDelimiterFactory()
				: protocolFactory;
	}

	@Override
	public GameEventHandlerFactory getGameEventHandlerFactory() {
		return gameEventHandlerFactory == null ? gameEventHandlerFactory = 
				new ConfigBasedGameEventHandlerFactory(this) : gameEventHandlerFactory;
	}
	
}