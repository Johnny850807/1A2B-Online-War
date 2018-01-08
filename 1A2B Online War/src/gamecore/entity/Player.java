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
	
	public static final Player SYSTEM(){
		Player system = new Player("¨t²Î");
		system.setId("sys");
		return system;
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
		System.out.println("Set player status " + getUserStatus() + " -> " + userStatus);
		this.userStatus = userStatus;
	}
	
	@Override
	public String toString() {
		return "Player: " + name + ", Status: " + userStatus;
	}
}
