package mock;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import container.Constants.Events.Chat;
import container.base.Client;
import container.protocol.Protocol;
import gamecore.entity.ChatMessage;
import gamecore.entity.Entity;

public class MockClient extends Entity implements Client{
	private List<Protocol> responses = new ArrayList<>();
	private Protocol lastedResponse;
	private Gson gson = new Gson();
	
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
}
