package gamecore.entity.room;

import gamecore.entity.BaseEntity;

public class Room extends BaseEntity{
	private String name;
	
	//TODO Room's game 
	
	public Room(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
