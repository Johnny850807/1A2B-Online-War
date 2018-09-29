package gamecore.model.games.a1b2.boss.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import container.core.MyLogger;
import container.core.Constants.Events.Chat;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.ChatMessage;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import gamecore.model.games.a1b2.boss.core.AttackResult;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;
import gamecore.model.games.a1b2.boss.core.Monster;
import gamecore.model.games.a1b2.boss.core.MonsterAction;
import gamecore.model.games.a1b2.boss.core.PlayerSpirit;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import gamecore.model.games.a1b2.imp.PossibleTableGuessing;

public class Lucid extends Monster{
	private static transient Player bossPlayer = new Player("Lucid");
	private static transient String[] CHATS = {"������", "�u�O�L��...", "�ڭn�{�u�q�F", "�o�@�ɯu�L��", "�ڬO���|�骺", 
			"�S����n�����", "�S����n�ˤ��o��", "�{�u�@�I���n��?", "�گu�O�����F...", "����", "......", "4", "4",
			"�]�\�a...", ":D", "�n�h..", "�ڪ��t��k�i�H�C�����q��", "����ɦV�O�p���a���j", "�A�̳��u�O�ƥ��Ӥw...", "�ڬO�X�۩󷬤���"
			, "�̤���...", "�xJAVA����}�o", "�x�]�p����", "�����������I", "�]���L�p���I", "�O�x�F�a�A..."};
	private transient ChangeAnswer changeAnswer;
	private transient SmartGuessingAttack smartGuessingAttack;
	private transient ChainAttack chainAttack;
	private transient ExplosionAttack explosionAttack;
	private transient SelfCuring selfCuring;
	private transient boolean hasUsedSelfCuring = false;
	private transient boolean hasSentOnGuessed4AMsg = false;
	private transient boolean hasSentSecondPhaseMsg = false;
	
	/**
	 * make the decision that if the boss has to change the answer
	 */
	private int changingAnswerDegree = 0; // change the answer if greater than 100
	
	public Lucid(MyLogger log, ProtocolFactory protocolFactory) {
		super("Lucid", 700, 1500, log, protocolFactory);
	}
	
	@Override
	public void init(IBoss1A2BGame game) {
		super.init(game);
		sendChatMessageToAllPlayers("�A�̲q�oĹ�ڶ�...����");
		int playerAmount = game.getPlayerSpirits().size();
		/**
		 * the hp/mp depends on the player amount:
		 */
		switch (playerAmount) {
		case 2:
			setMaxHp(2500);
			setMp(2500);
			break;
		case 3:
			setMaxHp(5300);
			setMp(3200);
			break;
		case 4:
			setMaxHp(8700);
			setMp(6500);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected List<MonsterAction> onCreateMonsterActions() {
		List<MonsterAction> actions = new ArrayList<>();
		actions.add(smartGuessingAttack = new SmartGuessingAttack());
		actions.add(chainAttack = new ChainAttack());
		actions.add(changeAnswer = new ChangeAnswer());
		actions.add(explosionAttack = new ExplosionAttack());
		actions.add(selfCuring = new SelfCuring());
		
		Map<String, PossibleTableGuessing> sharedMap = new HashMap<>();
		smartGuessingAttack.initStrategiesMap(sharedMap);
		chainAttack.initStrategiesMap(sharedMap);
		explosionAttack.initStrategiesMap(sharedMap);
		return actions;
	}
	

	@Override
	protected void onDamaging(AttackResult attackResult) {
		if (attackResult.getA() == 4)  //4A
			changingAnswerDegree += 100;
		else if (attackResult.getA() + attackResult.getB() == 4)  //4B, 2A2B, 3A1B, 1A3B...
			changingAnswerDegree += 33;
		else if (attackResult.getA() >= 3 || attackResult.getB() >= 3)  //3A, 3B
			changingAnswerDegree += 20;
		super.onDamaging(attackResult);
	}
	
	
	@Override
	protected MonsterAction chooseNextMonsterAction() {
		int playerAmount = game.getPlayerSpirits().size();
		
		// change the answer if the degree is greater than 100
		if (changingAnswerDegree >= 100 && getMp() >= changeAnswer.getCostMp())
			return changeAnswer;
		
		if (!hasUsedSelfCuring && playerAmount >= 3)  //self curing when have 3 or 4 players
			if (getHp() <= getMaxHp() / 7 && getMp() >= selfCuring.getCostMp())
			{
				hasUsedSelfCuring = true;
				sendChatMessageToAllPlayers("�ۧڴ_��I�I�I");
				return selfCuring;
			}
		
		//using the magic attack only if the hp left half
		if (getHp() <= getMaxHp() / 2 && getMp() >= chainAttack.getCostMp())
		{
			if (random.nextInt(100) < 50 || playerAmount == 1) //chain only if only one player
				return chainAttack;
			else
				return explosionAttack;
		}
		return smartGuessingAttack;
	}
	
	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		super.onAnswerGuessed4A(attackResult);
		if (!hasSentOnGuessed4AMsg && attackResult.getA() == 4)
		{
			hasSentOnGuessed4AMsg = true;
			sendChatMessageToAllPlayers("�A�̯u�����F�`����...�i��...");
		}
	}
	
	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {
		super.onSurvivedFromAttack(attackResult);
		if (!hasSentSecondPhaseMsg && !hasSentOnGuessed4AMsg && getHp() <= getMaxHp() / 2 )
		{
			hasSentSecondPhaseMsg = true;
			sendChatMessageToAllPlayers("�i�c�A�O�A�̹G�ڪ�...�I");
		}
		if (random.nextInt(100) >= 95)
			sendChatMessageToAllPlayers(CHATS[random.nextInt(CHATS.length)]);
	}
	
	@Override
	public void setAnswer(String answer) {
		super.setAnswer(answer);
		changingAnswerDegree = 0;  //remove the state whenever the answer changed
	}
	
	protected void sendChatMessageToAllPlayers(String msg){
		Protocol protocol = protocolFactory.createProtocol(Chat.SEND_MSG, RequestStatus.success.toString(), 
				gson.toJson(new ChatMessage(game.getRoomId(), bossPlayer, msg)));
		for (PlayerSpirit playerSpirit : game.getPlayerSpirits())
			playerSpirit.broadcast(protocol);
	}
}
