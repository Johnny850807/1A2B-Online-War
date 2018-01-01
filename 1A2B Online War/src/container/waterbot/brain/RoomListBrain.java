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
import utils.MyGson;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.google.gson.reflect.TypeToken;


/**
 * handle:
 * 	(1) updates the room list every 20 seconds
 *  (2) choose to join or create any room. ('set the game room reference if successfully.')
 */
public class RoomListBrain extends ChainBrain{
	private List<GameRoom> rooms;
	private static Random random = new Random();
	
	public RoomListBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	protected void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case SIGNIN:
			broadcastGetRoomListInRandomSeconds(waterBot, client);
			break;
		case GET_ROOMS:
			this.rooms = MyGson.parseGameRooms(protocol.getData());
			chooseToJoinOrCreate(waterBot, client);
			break;
		case CREATE_ROOM:
			enterAndSaveCreatedRoomIfMine(waterBot, protocol, client);
			break;
		case JOIN_ROOM:
			enterAndSaveJoinedRoomIfMine(waterBot, protocol, client);
			break;
		case BOOTED:
			updateStatusAndClearGameRoom(waterBot);
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}
	
	private void broadcastGetRoomListInRandomSeconds(WaterBot waterBot, Client client) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						if (waterBot.getMe().getUserStatus() == ClientStatus.signedIn)
						{
							log.trace(getLogPrefix(waterBot) + "requesting game roomlist...");
							Protocol protocol = protocolFactory.createProtocol(GET_ROOMS, REQUEST, null);
							client.broadcast(protocol);
						}
					}
				}, random.nextInt(10000)+1000);  //delay of each round
			}
		}, 10000, 10000);  //loop
	}
	
	private void enterAndSaveJoinedRoomIfMine(WaterBot waterBot, Protocol protocol, Client client)
	{
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (joinModel.getPlayer().equals(waterBot.getMe()))
		{
			log.trace(getLogPrefix(waterBot) + "joins the room successfully!");
			saveTheGameRoomToMemrory(waterBot, joinModel.getGameRoom());
		}
	}
	
	private void enterAndSaveCreatedRoomIfMine(WaterBot waterBot, Protocol protocol, Client client) {
		GameRoom gameRoom = MyGson.parseGameRoom(protocol.getData());
		if (gameRoom.isHost(waterBot.getMe()))
		{
			log.trace(getLogPrefix(waterBot) + "creates a room successfully!");
			saveTheGameRoomToMemrory(waterBot, gameRoom);
			closeRoomIfGameNotStartedIn10Minutes(waterBot, client);
		}
	}
	
	private void saveTheGameRoomToMemrory(WaterBot waterBot, GameRoom gameRoom){
		log.trace(getLogPrefix(waterBot) + "entering the room " + gameRoom.getName());
		waterBot.getMe().setUserStatus(ClientStatus.inRoom);
		waterBot.setGameRoom(gameRoom);
	}
	
	private void closeRoomIfGameNotStartedIn10Minutes(WaterBot waterBot, Client client){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				Player me = waterBot.getMe();
				GameRoom room = waterBot.getGameRoom();
				if (waterBot.imTheHost() && room.getRoomStatus() == RoomStatus.waiting &&
						me.getUserStatus() == ClientStatus.inRoom)
				{
					log.trace(getLogPrefix(waterBot) + "the room has long time not been started, so close it.");
					broadcastLeaveRoom(me, room, client);
				}
			}
		}, TimeUnit.MINUTES.toMillis(10));
	}
	
	private void broadcastLeaveRoom(Player player, GameRoom gameRoom, Client client){
		Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())));
		client.broadcast(protocol);
	}
	
	private void chooseToJoinOrCreate(WaterBot waterBot, Client client) {
		Player me = waterBot.getMe();
		boolean hasAvailableRoom = hasAvailableRoom();
		
		if (me.getUserStatus() == ClientStatus.signedIn)
		{
			if (hasAvailableRoom)
				broadcastJoinRequest(waterBot, client);
			else
			{
				log.debug(getLogPrefix(waterBot) + " there is no available room online, so create one.");
				broadcastCreateRoomRequest(waterBot, client);
			}
		}
		else
			log.trace(getLogPrefix(waterBot) + "is in any room or game.");
	}

	private void broadcastJoinRequest(WaterBot waterBot, Client client){
		GameRoom room = getNextRandomAvailableRoom();
		PlayerRoomIdModel playerRoomIdModel = new PlayerRoomIdModel(waterBot.getMe().getId(), room.getId());
		Protocol protocol = protocolFactory.createProtocol(JOIN_ROOM, REQUEST, gson.toJson(playerRoomIdModel));
		client.broadcast(protocol);
	}
	
	//TODO only duel mode now
	private void broadcastCreateRoomRequest(WaterBot waterBot, Client client){
		Protocol protocol = protocolFactory.createProtocol(CREATE_ROOM, REQUEST,
				gson.toJson(new GameRoom(GameMode.DUEL1A2B, "AI¶}©Ð^_^", waterBot.getMe())));
		client.broadcast(protocol);
	}
	
	private void updateStatusAndClearGameRoom(WaterBot waterBot){
		log.debug(getLogPrefix(waterBot) + "got booted.");
		waterBot.getMe().setUserStatus(ClientStatus.signedIn);
		waterBot.setGameRoom(null);
	}
	
	private boolean hasAvailableRoom(){
		return getNextRandomAvailableRoom() != null;
	}
	
	private GameRoom getNextRandomAvailableRoom(){
		List<GameRoom> availables = new ArrayList<>();
		for (GameRoom gameRoom : rooms)
			if (gameRoom.isAvailableToJoin())
				availables.add(gameRoom);
		if (availables.size() == 0)
			return null;
		return availables.get(random.nextInt(availables.size()));
	}
}
