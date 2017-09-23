package command.base;

import communication.message.Message;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.user.User;
import userservice.UserService;

public abstract class BaseCommand<T> implements Command{
	protected GameCore gamecore;
	protected UserService userService;
	protected Message<T> message;
	
	public BaseCommand(GameCore gamecore, UserService userService, Message<T> message) {
		this.gamecore = gamecore;
		this.userService = userService;
		this.message = message;
	}


}
