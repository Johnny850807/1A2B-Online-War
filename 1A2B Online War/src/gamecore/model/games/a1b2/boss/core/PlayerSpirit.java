package gamecore.model.games.a1b2.boss.core;

import container.core.MyLogger;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.ClientPlayer;
import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;
import utils.ForServer;

/**
 * @author ¦°¥Ã
 */
public class PlayerSpirit extends AbstractSpirit{
	private ClientPlayer player;
	
	public PlayerSpirit(ClientPlayer player, MyLogger log, ProtocolFactory protocolFactory) {
		super(player.getId(), player.getPlayerName(), 800, 0, log, protocolFactory);
		this.player = player;
	}
	
	public ClientPlayer getClientPlayer() {
		return player;
	}
	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}
	
	public void broadcast(Protocol protocol){
		player.broadcast(protocol);
	}
	
	@Override
	public Type getType() {
		return Type.PLAYER;
	}

	@Override
	@ForServer
	public void setAnswer(String answer) {
		log.trace("The player " + getName() + " set answer: " + answer);
		this.answer = answer;
	}
	
	@Override
	protected void onAnswerGuessed4A(AttackResult attackResult) {
		
	}
	
	@Override
	protected void onDie(AttackResult attackResult) {
		
	}
	
	@Override
	protected void onSurvivedFromAttack(AttackResult attackResult) {
		
	}
	
	
	@Override
	public int hashCode() {
		return player.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return player.equals(obj);
	}

}
