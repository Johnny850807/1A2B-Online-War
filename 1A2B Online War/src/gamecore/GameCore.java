package gamecore;

import java.util.List;

import command.Command;
import gamecore.entity.User;
import socket.UserService;

public interface GameCore {
	User signIn(UserService service, String name);
	void executeCommand(Command command);
}
