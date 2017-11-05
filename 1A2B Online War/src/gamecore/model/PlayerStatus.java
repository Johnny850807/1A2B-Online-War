package gamecore.model;

import gamecore.entity.Player;

public class PlayerStatus {
	private Player player;
	private boolean ready = false;
	
	public PlayerStatus(Player player) {
		this.player = player;
	}

	public PlayerStatus(Player player, boolean ready) {
		this.player = player;
		this.ready = ready;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	
}
