package utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import communication.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static <T extends Entity> Message<T> jsonToMessage(String json, Class<T> classOfData){
		Type eventType = new TypeToken<Message<T>>(){}.getType();
		Message<T> message = gson.fromJson(json, eventType);
		return message;
	}
	
	public static String messageToJson(Message<? extends Entity> message){
		return gson.toJson(message);
	}
}
