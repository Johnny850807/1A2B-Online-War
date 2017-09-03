package gamecore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import factory.GameFactory;
import gamecore.entity.Room;
import gamecore.entity.User;
import gamecore.entity.UserImp;
import socket.UserService;

public class GameCoreImp implements GameCore{
	private GameFactory factory;
	private List<User> onlineUsers = Collections.checkedList(new ArrayList<>(), User.class);
	private List<Room> roomList = Collections.checkedList(new ArrayList<>(), Room.class);
	
	public GameCoreImp(GameFactory factory) {
		this.factory = factory;
	}

	@Override
	public User signIn(UserService userService, String name){
		User user = factory.createUser(userService, name);
		onlineUsers.add(user);
		return user;
	}
	
	
}
