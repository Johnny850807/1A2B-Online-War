package container.waterbot.brain;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.waterbot.WaterBot;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;

public class SharedMemory {
	private static Logger log = LogManager.getLogger(SharedMemory.class);
	private Player me;
	private GameRoom room;
	private LinkedList<ChatMessage> chatmessages = new LinkedList<>();
	private WaterBot parent;
	
	public SharedMemory(WaterBot waterBot){
		this.parent = waterBot;
	}
	
	public WaterBot getParent() {
		return parent;
	}
	
	public Player getMe() {
		return me;
	}
	
	public void setMe(Player me) {
		this.me = me;
		log.trace("The bot " + parent.getName() + " updates the player : " + me);
	}
	
	public GameRoom getRoom() {
		return room;
	}
	
	public void setRoom(GameRoom room) {
		this.room = room;
		log.trace("The bot " + parent.getName() + " updates the room : " + room);
	}
	
	public List<ChatMessage> getChatmessages() {
		return chatmessages;
	}
	
	public void addMessage(ChatMessage msg){
		chatmessages.add(msg);
		if (chatmessages.size() > 30)
			chatmessages.removeFirst();
	}
	
	public ChatMessage getTheLastMessage(){
		return chatmessages.getLast();
	}
	
	
}
