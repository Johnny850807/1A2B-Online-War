package container.eventhandler.handlers.chat;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.Constants.Events.InRoom;
import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.eventhandler.handlers.inroom.BootPlayerHandler;
import container.eventhandler.handlers.inroom.ChangeStatusHandler;
import container.eventhandler.handlers.inroom.LeaveRoomHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.ServerConstant;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RequestStatus;
import gamecore.model.RoomStatus;

/**
 * @author Johnny850807
 * Input: the content of the chat message.
 * Output: the message with its unique id.
 */
public class SendChatMessageHandler extends GsonEventHandler<ChatMessage, ChatMessage>{
	private static Logger log = LogManager.getLogger(SendChatMessageHandler.class);
	private GameRoom room;
	public SendChatMessageHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ChatMessage> getDataClass() {
		return ChatMessage.class;
	}

	@Override
	protected Response onHandling(ChatMessage chatMessage) {
		chatMessage.initId();
		room = gameCore().getGameRoom(chatMessage.getGameRoomId());
		room.addChatMessage(chatMessage);
		checkGmCommand(chatMessage);
		return success(chatMessage);
	}

	private void checkGmCommand(ChatMessage chatMessage){
		Player player = chatMessage.getPoster();
		if (player.getName().equals(ServerConstant.GM_ACTUALNAM))
		{
			log.trace("Gm message.");
			parseGmCommand(chatMessage.getContent());
		}
	}
	
	private void parseGmCommand(String cmd){
		cmd = cmd.toLowerCase();
		try{
			if (cmd.contains("gm"))
			{
				log.trace("Is GM COMMAND.");
				String action = cmd.split(" ")[1];
				String value = cmd.split(" ")[2];
				if (action.equals("boot")) // "gm boot (index of the player)"
				{
					log.trace("COMMAND - BOOTING PLAYER: " + Integer.valueOf(value));
					if (room.getRoomStatus() != RoomStatus.gamestarted)
						bootPlayer(Integer.valueOf(value));
				}
				if (action.equals("status")) // "gm status (true|false)"
				{
					log.trace("COMMAND - CHANGING ALL PLAYERSTATUS TO: " + Boolean.valueOf(value));
					changeAllPlayerStatuses(Boolean.valueOf(value));
				}
			}
		}catch (Exception e) {
			log.error("GM command: '" + cmd + "' is incorrect.");
		}
	}
	
	private void bootPlayer(int index){
		Player player = room.getPlayerStatus().get(index).getPlayer();
		if (player.getName().equals(ServerConstant.GM_ACTUALNAM))
			log.trace("gm IS BOOTING SELF.");
		
		Protocol protocol = protocolFactory().createProtocol(InRoom.BOOTED, RequestStatus.request.toString(),
				gson.toJson(new PlayerRoomIdModel(player.getId(), room.getId())));
		new BootPlayerHandler(client(), protocol, gameCore(), protocolFactory()).handle();
	}
	
	private void changeAllPlayerStatuses(boolean ready){
		room.getPlayerStatus().parallelStream().forEach(ps -> {
			ChangeStatusModel csm = new ChangeStatusModel(ps.getPlayer().getId(), room.getId(), ready);
			Protocol protocol = protocolFactory().createProtocol(InRoom.CHANGE_STATUS, RequestStatus.request.toString(), 
					gson.toJson(csm));
			new ChangeStatusHandler(client(), protocol, gameCore(), protocolFactory()).handle();
		});
	}
	
	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().broadcastRoom(room.getId(), responseProtocol);
	}

}
