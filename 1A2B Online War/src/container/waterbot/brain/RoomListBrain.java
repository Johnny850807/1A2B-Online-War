package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.GameRoom;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import utils.GamecoreHelper;

import static container.Constants.*;
import static container.Constants.Events.*;
import static container.Constants.Events.Signing.*;
import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.google.gson.reflect.TypeToken;


public class RoomListBrain extends ChainBrain{
	private List<GameRoom> rooms;
	private static Random random = new Random();
	
	public RoomListBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	public synchronized void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
		switch (protocol.getEvent()) {
		case SIGNIN:
			if (protocol.getStatus().equals(SUCCESS)) 
				requestRoomList(client);
			break;
		case GET_ROOMS:
			if (protocol.getStatus().equals(SUCCESS)) 
			{
				saveGameRoomList(waterBot, protocol, client);
				joinToAnyRoom(waterBot, client);
			}
			break;
		case JOIN_ROOM:
			parseAndHandleJoinEvent(waterBot, protocol, client);
			break;
		case CLOSE_ROOM:
			GameRoom room = gson.fromJson(protocol.getData(), GameRoom.class);
			this.rooms.remove(room);
			break;
		case BOOTED:
			joinToAnotherRoomAfterSeconds(waterBot, client, 20000);
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void requestRoomList(Client client) {
		Protocol protocol = protocolFactory.createProtocol(GET_ROOMS, REQUEST, null);
		client.broadcast(protocol);
	}
	
	private void saveGameRoomList(WaterBot waterBot, Protocol protocol, Client client) {
		Type type = new TypeToken<List<GameRoom>>(){}.getType();
		this.rooms = gson.fromJson(protocol.getData(), type);
		log.trace(getLogPrefix(waterBot) + "Game room list got: " + GamecoreHelper.roomsToString(rooms));
	}
	
	private void parseAndHandleJoinEvent(WaterBot waterBot, Protocol protocol, Client client)
	{
		if (protocol.getStatus().equals(SUCCESS))
		{
			PlayerRoomModel joinModel = parsePlayerRoomModel(protocol.getData());
			if (joinModel.getPlayer().equals(waterBot.getMemory().getMe()))
				saveTheGameRoomToMemrory(waterBot, joinModel);
			else
				updateTheGameRoomsStatus(waterBot, joinModel.getGameRoom());
		}
		else
		{
			log.debug(getLogPrefix(waterBot) + "joined to the room unsuccessfully: " + protocol.getData());
			joinToAnotherRoomAfterSeconds(waterBot, client, 10000);
		}
	}
	
	private void joinToAnotherRoomAfterSeconds(WaterBot waterBot, Client client, long delay){
		log.trace(getLogPrefix(waterBot) + "after " + delay + "ms, the robot will request to join another room." );
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				joinToAnyRoom(waterBot, client);
			}
		}, delay);
	}
	
	private synchronized void joinToAnyRoom(WaterBot waterBot, Client client) {
		if (rooms.size() != 0)
		{
			GameRoom room = rooms.get(random.nextInt(rooms.size()));
			PlayerRoomIdModel playerRoomIdModel = new PlayerRoomIdModel(
					waterBot.getMemory().getMe().getId(), room.getId());
			Protocol protocol = protocolFactory.createProtocol(JOIN_ROOM, REQUEST, gson.toJson(playerRoomIdModel));
			client.broadcast(protocol);
			log.trace(getLogPrefix(waterBot) + " sends the join request to the room " + room.getName());
		}
	}
	
	private PlayerRoomModel parsePlayerRoomModel(String data){
		return gson.fromJson(data, PlayerRoomModel.class);
	}
	
	private synchronized void saveTheGameRoomToMemrory(WaterBot waterBot, PlayerRoomModel joinModel){
		log.trace(getLogPrefix(waterBot) + "entering the room " + joinModel.getGameRoom().getName());
		waterBot.getMemory().setRoom(joinModel.getGameRoom());
	}
	
	private synchronized void updateTheGameRoomsStatus(WaterBot waterBot, GameRoom room){
		rooms.remove(room);
		rooms.add(room);
	}
}
