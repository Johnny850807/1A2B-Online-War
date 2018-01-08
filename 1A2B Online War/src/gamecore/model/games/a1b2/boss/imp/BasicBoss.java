package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import container.core.MyLogger;
import container.core.Constants.Events.Chat;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.ChatMessage;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import utils.RandomString;

public class BasicBoss extends Monster{
	private transient Player bossPlayer;
	
	public BasicBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super("Boss", 3000, 1000, log, protocolFactory);
		bossPlayer = new Player(getName());
	}

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		//change the answer of the boss and broadcast
		setAnswer(onProduceAnswer());
		sendChatMessageToAllPlayers("不用得意，我還在讓！(改變密碼中...)");
	}

	@Override
	protected void onDie(AttackResult attackResult) {
		sendChatMessageToAllPlayers("可惡....");
	}


	protected void sendChatMessageToAllPlayers(String msg){
		Protocol protocol = protocolFactory.createProtocol(Chat.SEND_MSG, RequestStatus.success.toString(), 
				gson.toJson(new ChatMessage(game.getRoomId(), bossPlayer, msg)));
		for (PlayerSpirit playerSpirit : game.getPlayerSpirits())
			playerSpirit.broadcast(protocol);
	}
}
