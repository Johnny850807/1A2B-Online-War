package gamecore.entity;

public class Room extends Entity{
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
