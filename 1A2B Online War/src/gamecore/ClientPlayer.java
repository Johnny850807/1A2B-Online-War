package gamecore;

import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;

public class ClientPlayer {
	private Player player;
	private Client client;
	
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
	
	public String getClientAddress(){
		return client.getAddress();
	}
	
	public String getId(){
		return this.client.getId();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public void respondToClient(Protocol protocol){
		client.respond(protocol);
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
		return String.format("Client player info: id: %s, address:%s, player name:%s, player status:%s%n", 
				getId(), getClientAddress(), getPlayerName(), getPlayerStatus().toString());
	}
}
