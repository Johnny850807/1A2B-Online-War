package container.waterbot.brain;

import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.entity.GameRoom;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;
import gamecore.model.RoomStatus;
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
			saveTheJoinedRoomIfMe(waterBot, protocol);
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
		case LAUNCH_GAME:
			waterBot.getGameRoom().setRoomStatus(RoomStatus.gamestarted);
			waterBot.getMe().setUserStatus(ClientStatus.inGame);
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void saveTheCreatedRoomIfMine(WaterBot waterBot, Protocol protocol){
		GameRoom gameRoom = MyGson.parseGameRoom(protocol.getData());
		if (waterBot.isMe(gameRoom.getHost()))
			waterBot.setGameRoom(gameRoom);
	}
	
	private void saveTheJoinedRoomIfMe(WaterBot waterBot, Protocol protocol){
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (waterBot.isMe(joinModel))
			waterBot.setGameRoom(joinModel.getGameRoom());
	}

	private void addNewPlayerToRoomIfMyRoom(WaterBot waterBot, Protocol protocol) {
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (waterBot.getGameRoom() != null && waterBot.isMyRoom(joinModel) && !waterBot.isMe(joinModel))  //prevent from adding self
			waterBot.getGameRoom().addPlayer(joinModel.getPlayer());
	}
	
	private void leaveRoomIfMe(WaterBot waterBot, Protocol protocol){
		PlayerRoomModel leaveModel =  MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (waterBot.isMe(leaveModel))
			waterBot.clearGameRoom();
	}

	private void removePlayerFromRoomIfMyRoom(WaterBot waterBot, Protocol protocol) {
		PlayerRoomModel leaveModel =  MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		if (waterBot.getGameRoom() != null && waterBot.isMyRoom(leaveModel))
			waterBot.getGameRoom().removePlayer(leaveModel.getPlayer());
		
	}
	private void clearRoomIfMine(WaterBot waterBot, Protocol protocol){
		GameRoom gameRoom = MyGson.parseGameRoom(protocol.getData());
		if (waterBot.isMyRoom(gameRoom))
			waterBot.clearGameRoom();
	}
}
