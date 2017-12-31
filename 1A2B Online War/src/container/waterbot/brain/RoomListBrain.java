package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RoomStatus;
import utils.GamecoreHelper;

import static container.Constants.*;
import static container.Constants.Events.*;
import static container.Constants.Events.Signing.*;
import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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
				requestRoomListEvery20Seconds(waterBot, client);
			break;
		case GET_ROOMS:
			if (protocol.getStatus().equals(SUCCESS)) 
			{
				saveGameRoomList(waterBot, protocol, client);
				joinToAnyRoomOrCreateRoomIfNotInRoom(waterBot, client);
			}
			break;
		case CREATE_ROOM:
			parseAndHandleCreateRoomEvent(waterBot, protocol, client);
			break;
		case JOIN_ROOM:
			parseAndHandleJoinEvent(waterBot, protocol, client);
			break;
		case CLOSE_ROOM:
			GameRoom room = gson.fromJson(protocol.getData(), GameRoom.class);
			this.rooms.remove(room);
			break;
		case BOOTED:
			waterBot.getMemory().getMe().setUserStatus(ClientStatus.signedIn);
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void requestRoomListEvery20Seconds(WaterBot waterBot, Client client) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				log.trace(getLogPrefix(waterBot) + "requesting game roomlist...");
				Protocol protocol = protocolFactory.createProtocol(GET_ROOMS, REQUEST, null);
				client.broadcast(protocol);
			}
		}, 10000, 20000);
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
				saveTheGameRoomToMemroryAndEnterRoom(waterBot, joinModel.getGameRoom());
			else  // other game room has any player left, so update
				updateTheGameRoomsStatus(waterBot, joinModel.getGameRoom());
		}
		else
			log.debug(getLogPrefix(waterBot) + "joined to the room unsuccessfully: " + protocol.getData());
	}
	
	private void parseAndHandleCreateRoomEvent(WaterBot waterBot, Protocol protocol, Client client) {
		if (protocol.getStatus().equals(SUCCESS))
		{
			GameRoom gameRoom = gson.fromJson(protocol.getData(), GameRoom.class);
			waterBot.getMemory().setRoom(gameRoom);
			if (gameRoom.getHost().equals(waterBot.getMemory().getMe()))
			{
				log.trace(getLogPrefix(waterBot) + "created room successfully!");
				saveTheGameRoomToMemroryAndEnterRoom(waterBot, gameRoom);
				closeRoomIfGameNotStartedIn5Minutes(waterBot, client);
			}
		}
		else
			log.error(getLogPrefix(waterBot) + "create room failed.");
	}
	
	private void closeRoomIfGameNotStartedIn5Minutes(WaterBot waterBot, Client client){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Player me = waterBot.getMemory().getMe();
				GameRoom room = waterBot.getMemory().getRoom();
				if (me.getUserStatus() == ClientStatus.inRoom && room.getRoomStatus() == RoomStatus.waiting)
					if (room.getHost().equals(me)) {
						Protocol protocol = protocolFactory.createProtocol(CLOSE_ROOM, REQUEST, 
								gson.toJson(room));
						client.broadcast(protocol);
					}
			}
		}, TimeUnit.MINUTES.toMillis(5));
	}
	
	private synchronized void joinToAnyRoomOrCreateRoomIfNotInRoom(WaterBot waterBot, Client client) {
		Player me = waterBot.getMemory().getMe();
		boolean hasAvailableRoom = hasAvailableRoom();
		if (hasAvailableRoom && me.getUserStatus() == ClientStatus.signedIn )
			broadcastJoinRequest(waterBot, client);
		else if (!hasAvailableRoom && me.getUserStatus() == ClientStatus.signedIn)
		{
			broadcastCreateRoomRequest(waterBot, client);
			log.trace(getLogPrefix(waterBot) + " there is no room online.");
		}
	}

	private boolean hasAvailableRoom(){
		for (GameRoom gameRoom : rooms)
			if (gameRoom.getPlayerAmount() != gameRoom.getMaxPlayerAmount() && 
			gameRoom.getRoomStatus() != RoomStatus.gamestarted)
				return true;
		return false;
	}
	
	private GameRoom getNextAvailableRoom(){
		List<GameRoom> availables = new ArrayList<>();
		for (GameRoom gameRoom : rooms)
			if (gameRoom.getPlayerAmount() != gameRoom.getMaxPlayerAmount() && 
			gameRoom.getRoomStatus() != RoomStatus.gamestarted)
				availables.add(gameRoom);
		return availables.get(random.nextInt(availables.size()));
	}
	
	private void broadcastJoinRequest(WaterBot waterBot, Client client){
		GameRoom room = getNextAvailableRoom();
		PlayerRoomIdModel playerRoomIdModel = new PlayerRoomIdModel(
				waterBot.getMemory().getMe().getId(), room.getId());
		Protocol protocol = protocolFactory.createProtocol(JOIN_ROOM, REQUEST, gson.toJson(playerRoomIdModel));
		client.broadcast(protocol);
		log.trace(getLogPrefix(waterBot) + " sends the join request to the room " + room.getName());
	}
	
	//TODO only duel mode now
	private void broadcastCreateRoomRequest(WaterBot waterBot, Client client){
		Protocol protocol = protocolFactory.createProtocol(CREATE_ROOM, REQUEST,
				gson.toJson(new GameRoom(GameMode.DUEL1A2B, "AI©Ð", waterBot.getMemory().getMe())));
		client.broadcast(protocol);
	}
	
	private PlayerRoomModel parsePlayerRoomModel(String data){
		return gson.fromJson(data, PlayerRoomModel.class);
	}
	
	private synchronized void saveTheGameRoomToMemroryAndEnterRoom(WaterBot waterBot, GameRoom gameRoom){
		log.trace(getLogPrefix(waterBot) + "entering the room " + gameRoom.getName());
		waterBot.getMemory().getMe().setUserStatus(ClientStatus.inRoom);
		waterBot.getMemory().setRoom(gameRoom);
	}
	
	private synchronized void updateTheGameRoomsStatus(WaterBot waterBot, GameRoom room){
		rooms.remove(room);
		rooms.add(room);
	}
}
