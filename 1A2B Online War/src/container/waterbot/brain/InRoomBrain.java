package container.waterbot.brain;

import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;
import static container.Constants.Events.Games.ENTERGAME;

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

public class InRoomBrain extends BaseChatChainBrain{

	public InRoomBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	public synchronized void react(WaterBot waterBot, Protocol protocol, Client client) {
		super.react(waterBot, protocol, client);
		
		switch (protocol.getEvent()) {
		case LAUNCH_GAME:
			if (protocol.getStatus().equals(SUCCESS))
			{
				log.trace(getLogPrefix(waterBot) + "Launching game event received.");
				waterBot.getMemory().getMe().setUserStatus(ClientStatus.inGame);
				waterBot.getMemory().getRoom().setRoomStatus(RoomStatus.gamestarted);
				enterToTheGame(waterBot, client);
			}
			break;
		case JOIN_ROOM:
			parseJoinRoomAndHandle(waterBot, protocol, client);
			return;
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
				else if (leaveModel.getGameRoom().equals(waterBot.getMemory().getRoom()))
					waterBot.getMemory().getRoom().removePlayer(leaveModel.getPlayer());
			}
			break;
		case CHANGE_STATUS:
			if (protocol.getStatus().equals(SUCCESS)) {
				ChangeStatusModel model = gson.fromJson(protocol.getData(), ChangeStatusModel.class);
				updateStatusAndLaunchGameIfValid(waterBot, model, client);
			}
			return;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}

	private synchronized void parseJoinRoomAndHandle(WaterBot waterBot, Protocol protocol, Client client) {
		if (protocol.getStatus().equals(SUCCESS))
		{
			Player me = waterBot.getMemory().getMe();
			GameRoom room = waterBot.getMemory().getRoom();
			PlayerRoomModel joinModel = gson.fromJson(protocol.getData(), PlayerRoomModel.class);

			if (joinModel.getPlayer().equals(me)) 
				sayHelloAndLeaveIfTheRoomIsNotWorking(waterBot, joinModel.getGameRoom(), client);
			else if (room.getHost().equals(me))
			{
				room.addPlayer(joinModel.getPlayer());
				sendMessagDelayed(waterBot, joinModel.getGameRoom(), client, joinModel.getPlayer().getName() + 
						"，您好。歡迎加入我的遊戲！", 2000);		
			}
		}
	}
	
	private void sayHelloAndLeaveIfTheRoomIsNotWorking(WaterBot waterBot, GameRoom room, Client client){
		sendMessageRequest(waterBot, room, client, "大家好，我是 " + waterBot.getMemory().getMe().getName() + 
				"。是一個機器人，與你們一起玩遊戲是我的榮幸！");
		sendMessagDelayedWithStatusCondition(waterBot, room, client, "房主還在嗎？", 30000, ClientStatus.inRoom);
		sendMessagDelayedWithStatusCondition(waterBot, room, client, "今天似乎有點少人呢。", TimeUnit.MINUTES.toMillis(2), ClientStatus.inRoom);
		leaveRoomAfter3Minutes(waterBot, room, client);
		setReadyToGame(waterBot, room, client);
	}

	
	private void setReadyToGame(WaterBot waterBot, GameRoom room, Client client){
		log.trace(getLogPrefix(waterBot) + "set ready to game.");
		Player me = waterBot.getMemory().getMe();
		Protocol protocol = protocolFactory.createProtocol(CHANGE_STATUS, REQUEST, 
				gson.toJson(new ChangeStatusModel(me.getId(), room.getId(), true)));
		client.broadcast(protocol);
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
	
	private synchronized void updateStatusAndLaunchGameIfValid(WaterBot waterBot, ChangeStatusModel model, Client client) {
		GameRoom gameRoom = waterBot.getMemory().getRoom();
		Player me = waterBot.getMemory().getMe();
		Player player = gameRoom.getPlayerById(model.getPlayerId());
		gameRoom.changePlayerStatus(player, model.isPrepare());
		if (gameRoom.getHost().equals(me) && gameRoom.canStartTheGame())
		{
			sendMessageRequest(waterBot, gameRoom, client, "各位玩家～遊戲要開始囉！");
			launchTheGameAfter10Seconds(waterBot, gameRoom, client);
		}
	}
	
	private void launchTheGameAfter10Seconds(WaterBot waterBot, GameRoom gameRoom, Client client){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (gameRoom.canStartTheGame())
					broadcastLaunchingGameRequest(waterBot, gameRoom, client);
				else 
					sendMessageRequest(waterBot, gameRoom, client, "好吧又得等上一陣子。");
			}
		}, 10000);
	}
	
	private void broadcastLaunchingGameRequest(WaterBot waterBot, GameRoom gameRoom, Client client){
		if (gameRoom.getHost().equals(waterBot.getMemory().getMe()))
		{
			log.trace(getLogPrefix(waterBot) + "launching the game...");
			Protocol protocol = protocolFactory.createProtocol(LAUNCH_GAME, REQUEST, gson.toJson(gameRoom));
			client.broadcast(protocol);
		}
		else
			log.error(getLogPrefix(waterBot) + "The host is not the bot hiself, cannot launch the game....!!!");
	}
	
	private void enterToTheGame(WaterBot waterBot, Client client) {
		PlayerRoomIdModel model = new PlayerRoomIdModel(waterBot.getMemory().getMe().getId(), 
				waterBot.getMemory().getRoom().getId());
		Protocol protocol = protocolFactory.createProtocol(ENTERGAME, REQUEST, gson.toJson(model));
		client.broadcast(protocol);
	}
	
}
