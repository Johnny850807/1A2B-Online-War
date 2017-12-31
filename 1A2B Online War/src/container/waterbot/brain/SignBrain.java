package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.ServerInformation;

import static container.Constants.*;
import static container.Constants.Events.*;
import static container.Constants.Events.Signing.*;

import org.junit.runner.Request;

public class SignBrain extends ChainBrain{
	private Player player;
	private ServerInformation serverInfo;
	
	public SignBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	public synchronized void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
		switch (protocol.getEvent()) {
		case SIGNIN:
			parseAndSavePlayerToMemory(waterBot, protocol, client);
			requestServerInfo(client);
			break;
		case GETINFO:
			saveServerInfo(waterBot, protocol, client);
			break;
		case SIGNOUT:
			log.error("Sign out????");
			return;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void parseAndSavePlayerToMemory(WaterBot waterBot, Protocol protocol, Client client){
		if (protocol.getStatus().equals(SUCCESS))
		{
			Player player = gson.fromJson(protocol.getData(), Player.class);
			waterBot.getMemory().setMe(player);
		}
		else {
			log.error("Sign in unsuccessfully ! : " + protocol.getData());
		}
	}
	
	private void requestServerInfo(Client client){
		Protocol protocol = protocolFactory.createProtocol(GETINFO, REQUEST, null);
		client.broadcast(protocol);
	}
	
	private void saveServerInfo(WaterBot waterBot, Protocol protocol, Client client){
		this.serverInfo = gson.fromJson(protocol.getData(), ServerInformation.class);
		log.trace(getLogPrefix(waterBot) + "Server info got: " + serverInfo);
	}
	
}
