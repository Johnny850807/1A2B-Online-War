package container.waterbot;

import java.util.Collections;
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

public class WaterBot{
	private static Logger log = LogManager.getLogger(WaterBot.class);
	private static final Stack<String> NAMESTACK = new Stack<>();
	private Brain brain;
	private Client client;
	private String name;
	private int wid;
	private static int amount = 0;
	private SharedMemory memory = new SharedMemory(this);
	private LinkedBlockingQueue<Protocol> taskQueue = new LinkedBlockingQueue<>();
	private boolean running = true;
	
	static{
		NAMESTACK.push("Water");
		NAMESTACK.push("Fire");
		NAMESTACK.push("Joanna");
		NAMESTACK.push("Lin");
		NAMESTACK.push("ZonYee");;
		NAMESTACK.push("Nay");
		NAMESTACK.push("ShuYon");
		NAMESTACK.push("JAVA");
		NAMESTACK.push("Yuang");
		NAMESTACK.push("Python");
		Collections.shuffle(NAMESTACK);
		log.trace("Name is all prepared, size: " + NAMESTACK.size());
	}
	
	public WaterBot(Brain brain){
		this.wid = amount ++;
		this.brain = brain;
		
		log.trace("New WaterBot " + wid + " born !");
		if (NAMESTACK.isEmpty())
			throw new IllegalStateException("No name in the stack !");
		this.name = NAMESTACK.pop();
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
	
	public void setMe(Player me){
		log.trace(getName() + " signs in. Status -> " + ClientStatus.signedIn);
		getMemory().setMe(me);
	}
	
	public void setGameRoom(GameRoom gameRoom){
		log.trace(getName() + " holds the game room. Status -> " + ClientStatus.inRoom);
		getMe().setUserStatus(ClientStatus.inRoom);
		getMemory().setRoom(gameRoom);
	}
	
	public void clearGameRoom(){
		log.trace(getName() + " clears the game room. Status -> " + ClientStatus.signedIn);
		getMe().setUserStatus(ClientStatus.signedIn);
		getMemory().setRoom(null);
	}
	
	/**
	 * @return if the current player of the Waterbot playing is the host of 
	 * the current room of where the Waterbot staying.
	 */
	public boolean imTheHost(){
		return getGameRoom().getHost().equals(getMe());
	}
}
