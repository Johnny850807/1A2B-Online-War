package gamecore.model.games.a1b2.boss;

import gamecore.model.ClientPlayer;
import gamecore.model.games.a1b2.GuessResult;

public class AttackResult {
	private ClientPlayer player;
	private String guess;
	private GuessResult result;
	private int damage;
	
	public ClientPlayer getPlayer() {
		return player;
	}
	public void setPlayer(ClientPlayer player) {
		this.player = player;
	}
	public String getGuess() {
		return guess;
	}
	public void setGuess(String guess) {
		this.guess = guess;
	}
	public GuessResult getResult() {
		return result;
	}
	public void setResult(GuessResult result) {
		this.result = result;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	
}
