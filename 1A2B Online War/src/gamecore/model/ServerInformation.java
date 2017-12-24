package gamecore.model;

import container.Constants;

/**
 * The basic server information contains the amount of the online players or the rooms.
 */
public class ServerInformation implements Comparable<ServerInformation>{
	private int serverVersion; 
	private int onlineUserAmount;
	private int onlineRoomAmount;
	
	public ServerInformation(int serverVersion, int onlineUserAmount, int onlineRoomAmount) {
		this.serverVersion = serverVersion;
		this.onlineUserAmount = onlineUserAmount;
		this.onlineRoomAmount = onlineRoomAmount;
	}

	public int getServerVersion(){
		return serverVersion;
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
	public int compareTo(ServerInformation other) {
		return this.getOnlineRoomAmount() + this.getOnlineUserAmount() - other.getOnlineRoomAmount() - other.getOnlineUserAmount();
	}

	
}
