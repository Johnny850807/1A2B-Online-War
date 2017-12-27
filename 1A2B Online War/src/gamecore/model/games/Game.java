package gamecore.model.games;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.GameLifecycleListener;
import gamecore.model.GameMode;
import gamecore.model.MockLogger;
import utils.ForServer;

public abstract class Game {
	protected static transient Gson gson = new Gson();
	protected transient MyLogger log = new MockLogger();
	protected transient ProtocolFactory protocolFactory;
	protected transient Timer timer;
	protected transient GameLifecycleListener listener;
	private transient Map<Integer, TimeListener> timeListeners = new TreeMap<>();
	protected GameMode gameMode;
	protected Date launchDate = new Date();
	protected String roomId;
	protected long gameDuration = 2000;
	
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
 	
 	@ForServer
 	public void startGame(){
 		log.trace("Game " + gameMode.toString() + " started.");
 		listener.onGameStarted(this);
 		startTimer();
 	}

 	public void setLog(MyLogger log) {
		this.log = log;
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
	
	public Date getLaunchDate() {
		return launchDate;
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	public GameMode getGameMode() {
		return gameMode;
	}
	
	public long getGameDuration() {
		return gameDuration;
	}
	
	public void setGameLifecycleListener(GameLifecycleListener lifecycleListener){
		this.listener = lifecycleListener;
	}
	
}
