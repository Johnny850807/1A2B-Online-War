package mock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import container.Constants.Events.Chat;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.ChatMessage;
import gamecore.entity.Entity;
import utils.MyGson;

public class MockClient extends Entity implements Client{
	private static Logger log = LogManager.getLogger(MockClient.class);
	private List<Protocol> responses = new ArrayList<>();
	private Protocol lastedResponse;
	private Gson gson = MyGson.getGson();
	
	public MockClient() {
		initId();
	}
	
	public MockClient(String id) {
		setId(id);
	}
	
	@Override
	public void run() {}

	public List<Protocol> getResponses() {
		return responses;
	}
	
	public Protocol getLastedResponse() {
		return lastedResponse;
	}

	@Override
	public void disconnect() throws Exception {}



	@Override
	public void broadcast(Protocol protocol) {
		lastedResponse = protocol;
		responses.add(protocol);
		log.info("Client " + getId() + " broadcasted, msg: " + protocol);
	}

	@Override
	public String getAddress() {
		return "test client";
	}

	public List<ChatMessage> getReceivedMessages(){
		List<ChatMessage> messages = new ArrayList<>();
		for (Protocol protocol : responses)
			if (protocol.getEvent().equals(Chat.SEND_MSG))
				messages.add(gson.fromJson(protocol.getData(), ChatMessage.class));
		return messages;
	}
	
	public boolean hasReceivedEvent(String event){
		return responses.stream().anyMatch(p -> p.getEvent().equals(event));
	}
	
	public Protocol getLastedByEvent(String event){
		for (int i = responses.size() - 1 ; i > 0 ; i --)
			if (responses.get(i).getEvent().equals(event))
				return responses.get(i);
		throw new RuntimeException("Event not found");
	}
}
