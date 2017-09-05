package command;

import static communication.message.Event.signIn;
import static communication.message.Status.success;

import communication.message.Message;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import socket.UserService;
import utils.JsonConverter;

public class SignInCommand implements Command{
	private GameCore gamecore;
	private UserService userService;
	private Message<User> message;
	
	public SignInCommand(GameCore gamecore, UserService userService, Message<User> message) {
		this.gamecore = gamecore;
		this.userService = userService;
		this.message = message;
	}

	@Override
	public void execute() {
		// The user created is a proxy that will make the json serialization ugly.
		User user = gamecore.signIn(userService, message.getData().getName());
		
		// So I make a bean to contain only needed info
		UserImp userBean = new UserImp(user.getName());
		userBean.setId(user.getId());
		
		Message<User> message = new Message<>(signIn, success, userBean);
		user.sendMessage(message);
	}

}
