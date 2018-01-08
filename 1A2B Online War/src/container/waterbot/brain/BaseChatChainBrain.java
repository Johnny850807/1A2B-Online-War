package container.waterbot.brain;

import static container.core.Constants.Events.Chat.SEND_MSG;

import java.util.Timer;
import java.util.TimerTask;

import container.core.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;

public abstract class BaseChatChainBrain extends ChainBrain{

	public BaseChatChainBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}
	
	protected void sendMessageRequest(WaterBot waterBot, GameRoom room, Client client, String mgs){
		synchronized (waterBot) {
			if (waterBot.isMyRoom(room))  //i'm still in that room
			{
				Player me = waterBot.getMemory().getMe();
				ChatMessage message = new ChatMessage(room, me, mgs);
				Protocol protocol = protocolFactory.createProtocol(SEND_MSG, REQUEST, gson.toJson(message));
				client.broadcast(protocol);
			}
		}
	}
	
	protected void sendMessagDelayedWithStatusCondition(WaterBot waterBot, GameRoom room, 
			Client client, String msg, long delay, ClientStatus condition){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (waterBot) {
					if (waterBot.getMe().getUserStatus() == condition)
						sendMessageRequest(waterBot, room, client, msg);
				}
			}
		}, delay);
	}
	
	protected void sendMessagDelayed(WaterBot waterBot, GameRoom room, 
			Client client, String msg, long delay){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				sendMessageRequest(waterBot, room, client, msg);
			}
		}, delay);
	}
}
