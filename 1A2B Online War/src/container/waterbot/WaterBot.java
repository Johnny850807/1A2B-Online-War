package container.waterbot;

import java.util.Collections;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.protocol.Protocol;
import container.waterbot.brain.SharedMemory;

public class WaterBot{
	private static Logger log = LogManager.getLogger(WaterBot.class);
	private static final Stack<String> NAMESTACK = new Stack<>();
	private Brain brain;
	private Client client;
	private String name;
	private int wid;
	private static int amount = 0;
	private SharedMemory memory = new SharedMemory();
	
	static{
		NAMESTACK.push("Water");
		NAMESTACK.push("Fire");
		NAMESTACK.push("Joanna");
		NAMESTACK.push("Lin");
		NAMESTACK.push("ZonYee");;
		NAMESTACK.push("¤D·O");
		NAMESTACK.push("ShuYon");
		NAMESTACK.push("JAVA");
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
		log.trace("WaterBot " + wid + " receives protocol: " + protocol);
		try{
			brain.react(this, protocol, client);
		}catch (Exception e) {
			log.error(getName() + " - error", e);
		}
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
}
