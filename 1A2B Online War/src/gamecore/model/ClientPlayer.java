package gamecore.model;

import java.io.Serializable;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.Player;
import utils.ForServer;

public class ClientPlayer implements Serializable{
	private Player player;
	private transient Client client;
	
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
		client.broadcast(protocol);
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
