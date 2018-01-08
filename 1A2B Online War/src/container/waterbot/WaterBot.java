package container.waterbot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.protocol.Protocol;
import container.waterbot.brain.SharedMemory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomModel;

public class WaterBot{
	private static Logger log = LogManager.getLogger(WaterBot.class);
	public static String[] NAMES = {"Water", "Fire", "Joanna", "Lin", "ZonYee", "Nay", "ShuYin",
			"JAVA", "Yuang", "Python", "Gen", "June", "Grace", "Pick", "Nick", "Python", "Lay", "Ren"
			, "Yin", "Sean", "Hook", "Noy", "Esther", "Arping", "YanRu", "Yuan", "MinYun", "Coco"};
	private Brain brain;
	private Client client;
	private String name;
	private int wid;
	private static int amount = 0;
	private SharedMemory memory = new SharedMemory(this);
	private LinkedBlockingQueue<Protocol> taskQueue = new LinkedBlockingQueue<>();
	private boolean running = true;
	
	static{
		List<String> shuffledNames = Arrays.asList(NAMES);
		Collections.shuffle(shuffledNames);
		NAMES = shuffledNames.toArray(NAMES);
		log.trace("Name is all prepared, size: " + NAMES.length);
	}
	
	public WaterBot(Brain brain){
		this.wid = amount ++;
		this.brain = brain;
		
		log.trace("New WaterBot " + wid + " born !");
		if (NAMES.length == 0)
			throw new IllegalStateException("No name in the stack !");
		this.name = NAMES[this.wid];
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void receive(Protocol protocol){
		if (client == null)
			throw new IllegalStateException("The client should not be null");
		try {
			log.trace("WaterBot " + getName() + " receives protocol: " + protocol);
			taskQueue.put(protocol);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start(){
		new Thread(){
			@Override
			public void run() {
				currentThread().setName(getName());
				while (running) 
				{
					try {
						sleep(150);
						Protocol protocol = taskQueue.take();
						log.trace("WaterBot " + getName() + " handling protocol: " + protocol);
						brain.react(WaterBot.this, protocol, client);
					} catch (InterruptedException e) {}
					catch (Exception e) {
						log.error(getName() + " error" , e);
					}
				}
			}
		}.start();
	}
	
	public void stop(){
		running = false;
	}
	
	public Brain getBrain() {
		return brain;
	}

	public Client getClient() {
		return client;
	}

	public String getName() {
		return name;
	}

	public int getWid() {
		return wid;
	}

	public static int getAmount() {
		return amount;
	}
	
	public SharedMemory getMemory() {
		return memory;
	}
	
	public Player getMe(){
		return getMemory().getMe();
	}
	
	public GameRoom getGameRoom(){
		return getMemory().getRoom();
	}
	
	public boolean isInRoom() {
		return getGameRoom() != null;
	}
	
	public void setMe(Player me){
		log.trace(getName() + " signs in. Status -> " + me.getUserStatus());
		if (getMe() != null)
			throw new IllegalStateException("Already signed in.");
		getMemory().setMe(me);
	}
	
	public void setGameRoom(GameRoom gameRoom){
		if (isInRoom())
			throw new IllegalStateException("Already in room.");
		getMemory().setRoom(gameRoom);
		log.trace(getName() + " holds the game room. Status -> " + getMe().getUserStatus());
	}
	
	public void clearGameRoom(){
		if (getGameRoom() == null)
			throw new IllegalStateException("Has no room to clear.");
		getMe().setUserStatus(ClientStatus.signedIn);
		getMemory().setRoom(null);
		log.trace(getName() + " clears the game room. Status -> " + getMe().getUserStatus());
	}
	
	public boolean isMyRoom(GameRoom gameRoom) {
		return gameRoom.equals(getGameRoom());
	}
	
	public boolean isMyRoom(PlayerRoomModel model) {
		return model.getGameRoom().equals(getGameRoom());
	}
	
	public boolean isMe(Player player) {
		return getMe().equals(player);
	}
	
	public boolean isMe(PlayerRoomModel model) {
		return getMe().equals(model.getPlayer());
	}
	
	/**
	 * @return if the current player of the Waterbot playing is the host of 
	 * the current room of where the Waterbot staying.
	 */
	public boolean imTheHost(){
		return isInRoom() && getGameRoom().getHost().equals(getMe());
	}
}
