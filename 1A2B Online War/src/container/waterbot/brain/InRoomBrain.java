package container.waterbot.brain;

import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;

public class InRoomBrain extends ChainBrain{

	public InRoomBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	
	@Override
	public void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
		switch (protocol.getEvent()) {
		case LAUNCH_GAME:
			if (protocol.getStatus().equals(SUCCESS))
				waterBot.getMemory().getMe().setUserStatus(ClientStatus.inGame);
			break;
		case JOIN_ROOM:
			parseJoinRoomAndHandle(waterBot, protocol, client);
			break;
		case CLOSE_ROOM:
			if (protocol.getStatus().equals(SUCCESS))
				waterBot.getMemory().getMe().setUserStatus(ClientStatus.signedIn);
			break;
		case LEAVE_ROOM:
			if (protocol.getStatus().equals(SUCCESS))
			{
				PlayerRoomModel leaveModel = gson.fromJson(protocol.getData(), PlayerRoomModel.class);
				if (leaveModel.getPlayer().equals(waterBot.getMemory().getMe()))
					waterBot.getMemory().getMe().setUserStatus(ClientStatus.signedIn);
			}
		default:
			break;
		}
		
		nextIfNotNull(waterBot, protocol, client);
	}


	private void parseJoinRoomAndHandle(WaterBot waterBot, Protocol protocol, Client client) {
		if (protocol.getStatus().equals(SUCCESS))
		{
			PlayerRoomModel joinModel = gson.fromJson(protocol.getData(), PlayerRoomModel.class);
			if (joinModel.getPlayer().equals(waterBot.getMemory().getMe())) 
			{
				sendMessageRequest(waterBot, joinModel.getGameRoom(), client, "大家好，我是 " + waterBot.getMemory().getMe().getName() + 
						"。是一個機器人，與你們一起玩遊戲是我的榮幸 ☻☻！");
				sendMessagDelayedWithStatusCondition(waterBot, joinModel.getGameRoom(), client, "房主還在嗎？", 30000, ClientStatus.inRoom);
				sendMessagDelayedWithStatusCondition(waterBot, joinModel.getGameRoom(), client, "今天似乎有點少人呢。", TimeUnit.MINUTES.toMillis(3), ClientStatus.inRoom);
				leaveRoomAfter3Minutes(waterBot, joinModel.getGameRoom(), client);
				setReadyToGame(waterBot, joinModel.getGameRoom(), client);
			}
		}
	}
	
	private void sendMessageRequest(WaterBot waterBot, GameRoom room, Client client, String mgs){
		Player me = waterBot.getMemory().getMe();
		ChatMessage message = new ChatMessage(room, me, mgs);
		Protocol protocol = protocolFactory.createProtocol(SEND_MSG, REQUEST, gson.toJson(message));
		client.broadcast(protocol);
	}
	
	
	private void setReadyToGame(WaterBot waterBot, GameRoom room, Client client){
		log.trace(getLogPrefix(waterBot) + "set ready to game.");
		Player me = waterBot.getMemory().getMe();
		Protocol protocol = protocolFactory.createProtocol(CHANGE_STATUS, REQUEST, 
				gson.toJson(new ChangeStatusModel(me.getId(), room.getId(), true)));
		client.broadcast(protocol);
	}
	
	private void sendMessagDelayedWithStatusCondition(WaterBot waterBot, GameRoom room, 
			Client client, String msg, long delay, ClientStatus condition){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (waterBot.getMemory().getMe().getUserStatus() == condition)
					sendMessageRequest(waterBot, room, client, msg);
			}
		}, delay);
	}
	
	private void leaveRoomAfter3Minutes(WaterBot waterBot, GameRoom room, Client client){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				sendMessageRequest(waterBot, room, client, "抱歉，我先去別間看看唷！");
				PlayerRoomIdModel model = new PlayerRoomIdModel(waterBot.getMemory().getMe().getId(),
						room.getId());
				Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, gson.toJson(model));
				client.broadcast(protocol);
			}
		}, TimeUnit.MINUTES.toMillis(3));
	}
}
