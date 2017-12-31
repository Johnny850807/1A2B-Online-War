package container.waterbot.brain;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import utils.MyGson;

public abstract class ChainBrain implements Brain{
	protected static Logger log = LogManager.getLogger(ChainBrain.class);
	protected static Gson gson = MyGson.getGson();
	protected static String REQUEST = RequestStatus.request.toString();
	protected static String SUCCESS = RequestStatus.success.toString();
	protected static String FAILED = RequestStatus.failed.toString();
	protected static Timer timer = new Timer();
	protected Brain next;
	protected ProtocolFactory protocolFactory;
	
	public ChainBrain(Brain next, ProtocolFactory protocolFactory) {
		this.next = next;
		this.protocolFactory = protocolFactory;
	}
	
	@Override
	public synchronized void react(WaterBot waterBot, Protocol protocol, Client client) {
		delay(300);
	}
	
	protected void nextIfNotNull(WaterBot waterBot, Protocol protocol, Client client){
		if (next != null)
		{
			log.trace(getLogPrefix(waterBot) + "end thinking, propogate to the next chain node: " + next.getClass().getSimpleName());
			next.react(waterBot, protocol, client);
		}
		else
			log.trace(getLogPrefix(waterBot) + "end thinking, the chain reachs the end.");
	}
	
	protected String getLogPrefix(WaterBot bot){
		return "The bot " + bot.getName() + " - " + getClass().getSimpleName() + " ";
	}
	
	protected void delay(long times){
		try {Thread.sleep(times);} catch (InterruptedException e) {}
	}
	
}
