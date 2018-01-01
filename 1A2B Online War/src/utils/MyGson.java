package utils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ChangeStatusModel;
import gamecore.model.ServerInformation;
import gamecore.model.games.Game;
import gamecore.model.games.a1b2.Duel1A2BGame;
import gamecore.model.games.a1b2.Duel1A2BPlayerBarModel;

/**
 * @author Waterball
 * all Gson initializing, registering tasks and logics should be handled in here.
 */
public class MyGson {
	private static Gson gson;
	
	static{
		RuntimeTypeAdapterFactory<Game> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
			    .of(Game.class, "gameMode")
			    .registerSubtype(Duel1A2BGame.class, "DUEL1A2B");
		
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        		.registerTypeAdapter(Date.class, new DateDeserializer())
        		.registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
	}
	
	public static Gson getGson(){
		return gson;
	}

	public static <T> T parse(String json, Class<T> clazz){
		return getGson().fromJson(json, clazz);
	}
	
	public static GameRoom parseGameRoom(String json){
		return getGson().fromJson(json, GameRoom.class);
	}
	
	public static Player parsePlayer(String json){
		return getGson().fromJson(json, Player.class);
	}

	public static ChatMessage parseChatMessage(String json){
		return getGson().fromJson(json, ChatMessage.class);
	}
	
	public static List<GameRoom> parseGameRooms(String json){
		Type type = new TypeToken<List<GameRoom>>(){}.getType();
		return getGson().fromJson(json, type);
	}
	
	public static List<ChatMessage> parseChatMessages(String json){
		Type type = new TypeToken<List<ChatMessage>>(){}.getType();
		return getGson().fromJson(json, type);
	}
	
	/********Duel 1A2B********/
	
	public static List<Duel1A2BPlayerBarModel> parseDuel1A2BPlayerBarModels(String json){
		Type type = new TypeToken<List<Duel1A2BPlayerBarModel>>(){}.getType();
		return getGson().fromJson(json, type);
	}
	
	
}
