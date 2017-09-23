package utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import communication.message.Message;
import gamecore.entity.BaseEntity;
import gamecore.entity.Entity;

public class JsonConverter {
	private static Gson gson = new Gson();
	
	public static <T> Message<T> jsonToMessage(String json, Class<T> classOfData){
		Type eventType = new TypeToken<Message<T>>(){}.getType();
		Message<T> message = gson.fromJson(json, eventType);
		return message;
	}
	
	public static <T> T jsonToObject(String json, Class<T> classOfData) {
		return gson.fromJson(json, classOfData);
	}
	
	public static String objectToJson(Object object){
		return gson.toJson(object);
	}
	
	public static String messageToJson(Message<?> message){
		return gson.toJson(message);
	}
}
