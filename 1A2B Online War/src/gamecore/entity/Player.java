package gamecore.entity;

import gamecore.model.UserStatus;

/**
 * @author AndroidWork
 * Each Player a Online User contains a status and a name.
 */
public class Player extends Entity{
	private UserStatus userStatus = null;
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

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
	
	@Override
	public String toString() {
		return "User: " + name + ", Status: " + userStatus;
	}
}
