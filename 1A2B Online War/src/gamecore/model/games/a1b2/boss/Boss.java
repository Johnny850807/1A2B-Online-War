package gamecore.model.games.a1b2.boss;

import java.io.Serializable;
import java.util.List;

import gamecore.model.ClientPlayer;

public abstract class Boss implements Serializable{
	protected int hp;
	protected String answer;
	
	
	protected abstract void init();
	protected abstract void damage(ClientPlayer player, String guess);
	protected abstract void action(List<PlayerBlock> playerBlocks);
	
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
