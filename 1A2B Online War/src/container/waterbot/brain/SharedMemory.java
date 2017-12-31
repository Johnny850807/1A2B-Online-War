package container.waterbot.brain;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;

public class SharedMemory {
	private Player me;
	private GameRoom room;
	private LinkedList<ChatMessage> chatmessages = new LinkedList<>();
	
	public Player getMe() {
		return me;
	}
	
	public void setMe(Player me) {
		this.me = me;
	}
	
	public GameRoom getRoom() {
		return room;
	}
	
	public void setRoom(GameRoom room) {
		this.room = room;
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
