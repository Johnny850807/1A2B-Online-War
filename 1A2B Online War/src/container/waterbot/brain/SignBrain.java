package container.waterbot.brain;

import container.core.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;
import utils.MyGson;

import static container.core.Constants.*;
import static container.core.Constants.Events.*;
import static container.core.Constants.Events.Signing.*;

import org.junit.runner.Request;


/**
 * handle the sign in operation and the holding the server info
 */
public class SignBrain extends ChainBrain{
	private Player player;
	private ServerInformation serverInfo;
	
	public SignBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	protected  void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case SIGNIN:
			broadcastGetServerInfo(client);
			break;
		case GETINFO:
			this.serverInfo = MyGson.parse(protocol.getData(), ServerInformation.class);
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	
	private void broadcastGetServerInfo(Client client){
		Protocol protocol = protocolFactory.createProtocol(GETINFO, REQUEST, null);
		client.broadcast(protocol);
	}
	
}
