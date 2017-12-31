package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.PlayerRoomModel;

import static container.Constants.*;
import static container.Constants.Events.*;
import static container.Constants.Events.Signing.*;
import java.nio.file.Watchable;

import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;

public class InRoomBrain extends ChainBrain{

	public InRoomBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	
	@Override
	public synchronized void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
		switch (protocol.getEvent()) {
		case JOIN_ROOM:
			parseJoinRoomAndHandle(waterBot, protocol, client);
			break;

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
				sendHelloToRoom(waterBot, joinModel.getGameRoom(), client);
				setReadyToGame(waterBot, joinModel.getGameRoom(), client);
			}
		}
	}
	
	private void sendHelloToRoom(WaterBot waterBot, GameRoom room, Client client){
		Player me = waterBot.getMemory().getMe();
		ChatMessage message = new ChatMessage(room, me, "Hello, I'm " + me.getName() + ". :), it's my pleasure to play with u guys.");
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
}
