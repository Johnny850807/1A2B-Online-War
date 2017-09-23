package command.user;

import static communication.message.Event.signIn;
import static communication.message.Status.success;

import command.base.BaseCommand;
import command.base.Command;
import communication.message.Message;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import userservice.UserService;
import utils.JsonConverter;

public class SignInCommand extends BaseCommand<User> {

	public SignInCommand(GameCore gamecore, UserService userService, Message<User> message) {
		super(gamecore, userService, message);
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
