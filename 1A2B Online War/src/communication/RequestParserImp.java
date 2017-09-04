package communication;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gamecore.entity.Entity;
import gamecore.entity.User;
import gamecore.entity.UserImp;

public class RequestParserImp implements RequestParser{
	private Gson gson = new Gson();
	private static final String REGEX = "(sign)";
	
	@SuppressWarnings("rawtypes")
	@Override
	public Message<? extends Entity> parseRequest(String json) {
		Type eventType = new TypeToken<Message<UserImp>>(){}.getType();
		Message<UserImp> parseEvent = gson.fromJson(json, eventType);
		 
		parseEvent = null;
		/*if (parseEvent.getEvent().match(REGEX))
		{
			Type eventType = new TypeToken<Message<User>>(){}.getType();
			return gson.fromJson(json, eventType);
		}*/
		return parseEvent;
	}

}
