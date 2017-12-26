package gamecore.model.games.a1b2.boss;

import java.io.Serializable;

import gamecore.model.ClientPlayer;

/**
 * @author ¦°¥Ã
 */
public class PlayerBlock implements Serializable{
	private ClientPlayer player;
	private int hp;
	private String answer;
	
	
	public ClientPlayer getPlayer() {
		return player;
	}
	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
