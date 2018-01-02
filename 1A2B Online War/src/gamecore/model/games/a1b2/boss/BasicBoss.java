package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import container.Constants.Events.Chat;
import container.base.MyLogger;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.ChatMessage;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import utils.RandomString;

public class BasicBoss extends Monster{
	private Player player;
	
	public BasicBoss(MyLogger log, ProtocolFactory protocolFactory) {
		super(UUID.randomUUID().toString(), "Boss", log, protocolFactory);
		player = new Player(getName());
	}

	@Override
	public Type getType() {
		return Type.MONSTER;
	}

	@Override
	public int getMp() {
		return 1000;
	}

	@Override
	public int getMaxHp() {
		return 3000;
	}
	
	

	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		//change the answer of the boss and broadcast
		setAnswer(RandomString.nextNonDuplicatedNumber(4));
		sendChatMessageToAllPlayers("不用得意，我還在讓!(改變密碼中...)");
	}

	@Override
	protected void onDie(AttackResult attackResult) {
		
	}

	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {
		
	}

	@Override
	protected List<MonsterAction> createMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(new NormalAttack());
		return actions;
	}

	protected void sendChatMessageToAllPlayers(String msg){
		Protocol protocol = protocolFactory.createProtocol(Chat.SEND_MSG, RequestStatus.success.toString(), 
				gson.toJson(new ChatMessage(game.getRoomId(), new Player(getName()), msg)));
		for (PlayerSpirit playerSpirit : game.getPlayerSpirits())
			playerSpirit.broadcast(protocol);
	}
}
