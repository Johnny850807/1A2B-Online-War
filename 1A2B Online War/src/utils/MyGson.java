package utils;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gamecore.model.games.Game;
import gamecore.model.games.a1b2.Duel1A2BGame;

public class MyGson {
	public static Gson getGson(){
		RuntimeTypeAdapterFactory<Game> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
			    .of(Game.class, "gameMode")
			    .registerSubtype(Duel1A2BGame.class, "DUEL1A2B");
		
		return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        		.registerTypeAdapter(Date.class, new DateDeserializer())
        		.registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
	}
}
