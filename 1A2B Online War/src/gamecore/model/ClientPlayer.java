package gamecore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.LeisureTimeChallengeable;
import gamecore.entity.Player;
import utils.ForServer;

public class ClientPlayer implements Serializable, LeisureTimeChallengeable{
	private static final long LEISURE_TIME_EXPIRE = TimeUnit.MINUTES.toMillis(1);
	private transient Client client;
	private transient Date createdTime = new Date();
	private transient Date latestLeisureTime = new Date();
	private Player player;
	
	public ClientPlayer(Client client, Player player) {
		this.client = client;
		this.player = player;
		assert client.getId().equals(player.getId()) : "The binded client's id is not equal to the player's id.";
	}
	
	public String getPlayerName(){
		return player.getName();
	}
	
	public ClientStatus getPlayerStatus(){
		return player.getUserStatus();
	}
	
	@ForServer
	public String getClientAddress(){
		return client == null ? "(cannot show the address)" : client.getAddress();
	}
	
	public String getId(){
		return this.player.getId();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@ForServer
	public Client getClient() {
		return client;
	}
	
	@ForServer
	public void setClient(Client client) {
		this.client = client;
	}
	
	@ForServer
	public void broadcast(Protocol protocol){
		/**
		 * if the player is in the room list, the leisure time start counted,
		 * so that he should choose any room to join or create self room in the expired time given.
		 * Until he is not in the room list, the status will be changed, then any broadcast will help him to
		 * push the latest leisure time.
		 */
		if (getPlayer().getUserStatus() != ClientStatus.signedIn)
			pushLeisureTime();
		getClient().broadcast(protocol);
	}
	
	@Override
	@ForServer
	public synchronized boolean isLeisureTimeExpired(){
		long diffTime = System.currentTimeMillis() - latestLeisureTime.getTime();
		return diffTime >= LEISURE_TIME_EXPIRE;
	}

	@Override
	@ForServer
	public void pushLeisureTime(){
		latestLeisureTime = new Date();
	}
	
	@Override
	public boolean equals(Object obj) {
		return getId().equals(((ClientPlayer)obj).getId());
	}
	
	@Override
	public int hashCode() {
		return player.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("id: %s, player name:%s, address:%s, player status:%s%n", 
				getId(), getPlayerName(), getClientAddress(), getPlayerStatus().toString());
	}
}
