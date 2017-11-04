package gamefactory;

import container.ServiceIO;
import container.SocketService;
import container.UserService;
import container.protocol.ProtocolFactory;
import container.protocol.XOXOXDelimiterFactory;
import gamecore.GameCore;
import gamecore.ReleaseGameCore;

public class GameOnlineReleaseFactory implements GameFactory{
	private ProtocolFactory protocolFactory;
	private GameCore gameCore;

	
	@Override
	public GameCore getGameCore() {
		return gameCore == null ? gameCore = new ReleaseGameCore(this) : gameCore;
	}
	
	@Override
	public UserService createService(ServiceIO io) {
		return new SocketService(this, io);
	}

	@Override
	public ProtocolFactory getProtocolFactory() {
		return protocolFactory == null ? protocolFactory = new XOXOXDelimiterFactory()
				: protocolFactory;
	}
	
}
