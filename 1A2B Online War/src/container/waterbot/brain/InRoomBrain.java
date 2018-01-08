package container.waterbot.brain;

import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;
import static container.Constants.Events.Games.ENTERGAME;

import java.util.Timer;
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
import gamecore.model.PlayerStatus;
import gamecore.model.RoomStatus;
import gamecore.model.games.Game;
import utils.MyGson;

public class InRoomBrain extends BaseChatChainBrain{

	public InRoomBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	protected void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case CREATE_ROOM:
			if (waterBot.imTheHost())
				closeRoomIfGameNotStartedIn10Minutes(waterBot, client);
			return;
		case JOIN_ROOM:
			if (waterBot.imTheHost())
				sendWelcomeMessage(waterBot, protocol, client);
			setReadyIfMe(waterBot, protocol, client);
			return;
		case CHANGE_STATUS:
			ChangeStatusModel model = gson.fromJson(protocol.getData(), ChangeStatusModel.class);
			updateStatusAndLaunchGameIfAvailable(waterBot, model, client);
			return;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private void closeRoomIfGameNotStartedIn10Minutes(WaterBot waterBot, Client client){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (waterBot) {
					Player me = waterBot.getMe();
					GameRoom room = waterBot.getGameRoom();
					if (waterBot.isInRoom() && room.getRoomStatus() == RoomStatus.waiting 
							&& waterBot.imTheHost())
					{
						log.trace(getLogPrefix(waterBot) + "the room has long time not been started, so close it.");
						broadcastLeaveRoom(me, room, client);
					}
				}
			}
		}, TimeUnit.MINUTES.toMillis(5));
	}

	//TODO inroom
	private void broadcastLeaveRoom(Player player, GameRoom gameRoom, Client client){
		Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, 
				gson.toJson(new PlayerRoomIdModel(player.getId(), gameRoom.getId())));
		client.broadcast(protocol);
	}
	
	private void sendWelcomeMessage(WaterBot waterBot, Protocol protocol, Client client) {
		PlayerRoomModel joinModel = MyGson.parse(protocol.getData(), PlayerRoomModel.class);
		Player me = waterBot.getMe();
		GameRoom room = waterBot.getGameRoom();
			sendMessagDelayed(waterBot, joinModel.getGameRoom(), client, joinModel.getPlayer().getName() + 
					"，您好。歡迎加入我的遊戲！", 2000);		

		if (waterBot.isMe(joinModel))
			sayHelloAndLeaveIfTheRoomIsNotWorking(waterBot, room, client);
	}
	
	private void sayHelloAndLeaveIfTheRoomIsNotWorking(WaterBot waterBot, GameRoom room, Client client){
		sendMessageRequest(waterBot, room, client, "大家好，我是 " + waterBot.getMemory().getMe().getName() + 
				"。是一個機器人，與你們一起玩遊戲是我的榮幸！");
		sendMessagDelayedWithStatusCondition(waterBot, room, client, "房主還在嗎？", 30000, ClientStatus.inRoom);
		sendMessagDelayedWithStatusCondition(waterBot, room, client, "今天似乎有點少人呢。", TimeUnit.MINUTES.toMillis(2), ClientStatus.inRoom);
		setReadyToGame(waterBot, room, client);
		leaveRoomAfter3Minutes(waterBot, room, client);
	}
	
	private void setReadyIfMe(WaterBot waterBot, Protocol protocol, Client client) {
		PlayerRoomModel joinModel = gson.fromJson(protocol.getData(), PlayerRoomModel.class);
		if (waterBot.isMe(joinModel))
			setReadyToGame(waterBot, joinModel.getGameRoom(), client);
	}
	
	private void setReadyToGame(WaterBot waterBot, GameRoom room, Client client){
		Player me = waterBot.getMe();
		Protocol protocol = protocolFactory.createProtocol(CHANGE_STATUS, REQUEST, 
				gson.toJson(new ChangeStatusModel(me.getId(), room.getId(), true)));
		client.broadcast(protocol);
	}
	
	
	private void leaveRoomAfter3Minutes(WaterBot waterBot, GameRoom room, Client client){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				sendMessageRequest(waterBot, room, client, "抱歉，我先去別間看看唷！");
				PlayerRoomIdModel model = new PlayerRoomIdModel(waterBot.getMe().getId(), room.getId());
				Protocol protocol = protocolFactory.createProtocol(LEAVE_ROOM, REQUEST, gson.toJson(model));
				client.broadcast(protocol);
			}
		}, TimeUnit.MINUTES.toMillis(3));
	}
	
	private void updateStatusAndLaunchGameIfAvailable(WaterBot waterBot, ChangeStatusModel model, Client client) {
		synchronized (waterBot) {
			GameRoom gameRoom = waterBot.getGameRoom();
			Player me = waterBot.getMe();
			Player player = gameRoom.getPlayerById(model.getPlayerId());
			gameRoom.changePlayerStatus(player, model.isPrepare());
			if (waterBot.imTheHost() && gameRoom.canStartTheGame())
			{
				sendMessageRequest(waterBot, gameRoom, client, "各位玩家～遊戲要開始囉！");
				launchTheGameAfter10Seconds(waterBot, gameRoom, client);
			}
		}
	}
	
	private void launchTheGameAfter10Seconds(WaterBot waterBot, GameRoom gameRoom, Client client){
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (waterBot) {
					if (waterBot.isInRoom() && waterBot.imTheHost() && gameRoom.canStartTheGame())
						broadcastLaunchingGameRequest(waterBot, gameRoom, client);
					else 
						sendMessageRequest(waterBot, gameRoom, client, "好吧又得等上一陣子。");
				}
			}
		}, 10000);
	}
	
	private void broadcastLaunchingGameRequest(WaterBot waterBot, GameRoom gameRoom, Client client){
		log.trace(getLogPrefix(waterBot) + "launching the game...");
		Protocol protocol = protocolFactory.createProtocol(LAUNCH_GAME, REQUEST, gson.toJson(gameRoom));
		client.broadcast(protocol);
	}
	
	
}
