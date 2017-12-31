package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;

public class Duel1A2BGameBrain extends ChainBrain{

	public Duel1A2BGameBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}
	
	@Override
	public void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
	}

}
