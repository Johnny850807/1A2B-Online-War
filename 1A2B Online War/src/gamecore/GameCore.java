package gamecore;

import java.util.List;

import command.base.Command;
import gamecore.entity.room.Room;
import gamecore.entity.user.User;
import userservice.UserService;

public interface GameCore {
	User signIn(UserService service, String name);
	void executeCommand(Command command);
	List<User> getOnlineUsers();
	List<Room> getRoomList();
}
