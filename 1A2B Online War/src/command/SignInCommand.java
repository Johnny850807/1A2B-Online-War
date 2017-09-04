package command;

import communication.Message;
import communication.Status;
import gamecore.GameCore;
import gamecore.entity.User;
import socket.UserService;
import static communication.Event.*;
import static communication.Status.*;

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
		User user = gamecore.signIn(userService, message.getData().getName());
		Message<User> message = new Message<>(signIn, success, user);
		user.sendMessage(message);
	}

}
