package communication.message;

import communication.protocol.Protocol;
import utils.JsonConverter;

public class MessageUtils {
	public static Message<String> protocolToMessage(Protocol protocol){
		Event event = Event.valueOf(protocol.getEvent());
		Status status = Status.valueOf(protocol.getStatus());
		String data = protocol.getData();
		return new Message<>(event, status, data);
	}
	
	public static <T> Message<T> protocolToMessage(Protocol protocol, Class<? extends T> clazz){
		Event event = Event.valueOf(protocol.getEvent());
		Status status = Status.valueOf(protocol.getStatus());
		T data = JsonConverter.jsonToObject(protocol.getData(), clazz);
		return new Message<>(event, status, data);
	}
}
