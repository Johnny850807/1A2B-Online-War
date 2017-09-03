package command;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gamecore.entity.UserImp;

public class Main {

	public static void main(String[] args) {
		String json = "{"+
    "\"event\": \"signIn\","+
    "\"data\": {	"+
        "\"name\" : \"¨Óª±³á\""+
    "}"+
"}";
		System.out.println(json);
		Type eventType = new TypeToken<Message<UserImp>>(){}.getType();
		Message<UserImp> user = new Gson().fromJson(json, eventType);
		System.out.println(user.getData().getName());
	}

}
