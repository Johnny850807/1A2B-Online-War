package gamecore.model.gamemodels;

import java.util.Date;

public abstract class GameModel {
	private Date luanchDate = new Date();
	
	public GameModel() {}

 	public GameModel(Date luanchDate) {
 		this.luanchDate = luanchDate;
	}

	public static class GameInfo{
		private Date launchTime;

		public GameInfo(Date launchTime) {
			this.launchTime = launchTime;
		}
		
		public GameInfo() {
			this.launchTime = new Date();
		}

		public Date getLaunchTime() {
			return launchTime;
		}

		public void setLaunchTime(Date launchTime) {
			this.launchTime = launchTime;
		}

	}
}
