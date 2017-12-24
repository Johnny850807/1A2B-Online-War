package gamecore.model.games;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import com.google.gson.Gson;

import container.protocol.ProtocolFactory;
import gamecore.model.GameMode;

public abstract class Game {
	protected static transient Gson gson = new Gson();
	protected GameMode gameMode;
	protected Date launchDate = new Date();
	protected String roomId;
	protected long gameDuration = 2000;
	protected transient ProtocolFactory protocolFactory;
	protected transient Timer timer;
	private transient Map<Integer, TimeListener> timeListeners = new TreeMap<>();
	
	public Game(ProtocolFactory protocolFactory, GameMode gameMode, String roomId) {
		this.gameMode = gameMode;
		this.roomId = roomId;
		this.protocolFactory = protocolFactory;
		timer = new Timer(gameMode.toString());
	}

 	public Game(ProtocolFactory protocolFactory, GameMode gameMode, String roomId, Date luanchDate) {
 		this(protocolFactory, gameMode, roomId);
 		this.launchDate = luanchDate;
	}
 	
 	public void startGame(){
 		startTimer();
 	}

	private void startTimer() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				gameDuration += 1000;
				if (timeListeners.containsKey(gameDuration))
					timeListeners.get(gameDuration).onTime(gameDuration);
			}
		}, 2000, 1000);
	}
	
	public interface TimeListener{
		public void onTime(long gameDuration);
	}
}
