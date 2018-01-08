package container.waterbot.brain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import container.core.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.model.RequestStatus;
import utils.MyGson;

public abstract class ChainBrain implements Brain{
	protected static Logger log = LogManager.getLogger(ChainBrain.class);
	protected static Gson gson = MyGson.getGson();
	protected static String REQUEST = RequestStatus.request.toString();
	protected static String SUCCESS = RequestStatus.success.toString();
	protected static String FAILED = RequestStatus.failed.toString();
	protected Brain next;
	protected ProtocolFactory protocolFactory;
	
	public ChainBrain(Brain next, ProtocolFactory protocolFactory) {
		this.next = next;
		this.protocolFactory = protocolFactory;
	}
	
	@Override
	public void react(WaterBot waterBot, Protocol protocol, Client client) {
		synchronized (waterBot) {
			if (protocol.getStatus().equals(SUCCESS))
				onReceiveSuccessProtocol(waterBot, protocol, client);
			else if (protocol.getStatus().equals(FAILED))
				onReceiveFailedProtocol(waterBot, protocol, client);
			else 
				throw new IllegalStateException(getLogPrefix(waterBot) + "WTF that the status is not SUCCESS or even FAILED?");
		}
	}
	
	protected abstract void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client);
	
	protected synchronized void onReceiveFailedProtocol(WaterBot waterBot, Protocol protocol, Client client){
		log.error(getLogPrefix(waterBot) + "Failed protocol received: " + protocol);
	}
	
	protected void nextIfNotNull(WaterBot waterBot, Protocol protocol, Client client){
		if (next != null)
		{
			//log.trace(getLogPrefix(waterBot) + "end thinking, propogate to the next chain node: " + next.getClass().getSimpleName());
			next.react(waterBot, protocol, client);
		}
		else
			/*log.trace(getLogPrefix(waterBot) + "end thinking, the chain reachs the end.")*/;
	}
	
	protected String getLogPrefix(WaterBot bot){
		return "The bot " + bot.getName() + " - " + getClass().getSimpleName() + " ";
	}
	
	protected void delay(long times){
		try {Thread.sleep(times);} catch (InterruptedException e) {}
	}
	
}
