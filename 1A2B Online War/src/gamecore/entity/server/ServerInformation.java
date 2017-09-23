package gamecore.entity.server;

import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public class ServerInformation implements Entity{
	private int onlineUserAmount;
	private int onlineRoomAmount;
	
	public ServerInformation(int onlineUserAmount, int onlineRoomAmount) {
		this.onlineUserAmount = onlineUserAmount;
		this.onlineRoomAmount = onlineRoomAmount;
	}

	public int getOnlineUserAmount() {
		return onlineUserAmount;
	}

	public void setOnlineUserAmount(int onlineUserAmount) {
		this.onlineUserAmount = onlineUserAmount;
	}

	public int getOnlineRoomAmount() {
		return onlineRoomAmount;
	}

	public void setOnlineRoomAmount(int onlineRoomAmount) {
		this.onlineRoomAmount = onlineRoomAmount;
	}

	@Override
	public int compareTo(Entity o) {
		ServerInformation other = (ServerInformation)o;
		return this.getOnlineRoomAmount() + this.getOnlineUserAmount() - other.getOnlineRoomAmount() - other.getOnlineUserAmount();
	}

	@Override
	public void setId(String id) {
		throw new IllegalStateException("Not Supported.");
	}

	@Override
	public String getId() {
		throw new IllegalStateException("Not Supported.");
	}
	
	
}
