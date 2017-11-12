package gamecore.entity;

import gamecore.model.ClientStatus;

/**
 * @author AndroidWork
 * Each Player a Online User contains a status and a name.
 */
public class Player extends Entity{
	private ClientStatus userStatus = null;
	private String name;
	
	public Player(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClientStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(ClientStatus userStatus) {
		this.userStatus = userStatus;
	}
	
	@Override
	public String toString() {
		return "User: " + name + ", Status: " + userStatus;
	}
}
