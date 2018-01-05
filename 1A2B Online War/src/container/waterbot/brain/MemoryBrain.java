package container.waterbot.brain;

import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;
import utils.MyGson;

import static container.core.Constants.*;
import static container.core.Constants.Events.*;
import static container.core.Constants.Events.InRoom.*;
import static container.core.Constants.Events.RoomList.*;
import static container.core.Constants.Events.Signing.*;

import container.core.Client;

/**
 * The brain handling all the events that have to modify the reference from the memory of the waterbot.
 */
public class MemoryBrain extends ChainBrain{

	public MemoryBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	protected void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case SIGNIN:
			waterBot.setMe(MyGson.parsePlayer(protocol.getData()));
			break;
		case CREATE_ROOM:
			saveTheCreatedRoomIfMine(waterBot, protocol);
			break;
		case JOIN_ROOM:
			saveTheJoinedRoomIfMine(waterBot, protocol);
			addNewPlayerToRoomIfMyRoom(waterBot, protocol);
			break;
		case BOOTED:
			leaveRoomIfMe(waterBot, protocol);
			break;
		case LEAVE_ROOM:
			leaveRoomIfMe(waterBot, protocol);
			removePlayerFromRoomIfMyRoom(waterBot, protocol);
			break;
		case CLOSE_ROOM:
			clearRoomIfMine(waterBot, protocol);
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void saveTheCreatedRoomIfMine(WaterBot waterBot, Protocol protocol){
		GameRoom gameRoom = MyGson.parseGameRoom(protocol.getData());
		if (gameRoom.isHost(waterBot.getMe()))
			waterBot.setGameRoom(gameRoom);
	}
	
	private void saveTheJoinedRoomIfMine(WaterBot waterBot, Protocol protocol){
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (joinModel.getPlayer().equals(waterBot.getMe()))
			waterBot.setGameRoom(joinModel.getGameRoom());
	}

	private void addNewPlayerToRoomIfMyRoom(WaterBot waterBot, Protocol protocol) {
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (joinModel.getGameRoom().equals(waterBot.getGameRoom()))
			waterBot.getGameRoom().addPlayer(joinModel.getPlayer());
	}
	
	private void leaveRoomIfMe(WaterBot waterBot, Protocol protocol){
		PlayerRoomModel leaveModel =  MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (leaveModel.getPlayer().equals(waterBot.getMe()))
			waterBot.clearGameRoom();
	}

	private void removePlayerFromRoomIfMyRoom(WaterBot waterBot, Protocol protocol) {
		PlayerRoomModel leaveModel =  MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (leaveModel.getGameRoom().equals(waterBot.getGameRoom()))
			waterBot.getGameRoom().removePlayer(leaveModel.getPlayer());
		
	}
	private void clearRoomIfMine(WaterBot waterBot, Protocol protocol){
		GameRoom gameRoom = MyGson.parseGameRoom(protocol.getData());
		if (gameRoom.equals(waterBot.getGameRoom()))
			waterBot.clearGameRoom();
	}
}
