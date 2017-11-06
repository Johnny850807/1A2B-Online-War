package gamecore.model;

/**
 * The basic server information contains the amount of the online players or the rooms.
 */
public class ServerInformation implements Comparable<ServerInformation>{
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
	public int compareTo(ServerInformation other) {
		return this.getOnlineRoomAmount() + this.getOnlineUserAmount() - other.getOnlineRoomAmount() - other.getOnlineUserAmount();
	}

	
}
