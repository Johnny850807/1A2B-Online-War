package command.server;

import org.omg.PortableInterceptor.SUCCESSFUL;

import command.base.BaseCommand;
import command.base.Command;
import communication.message.Event;
import communication.message.Message;
import communication.message.Status;
import gamecore.GameCore;
import gamecore.entity.server.ServerInformation;
import userservice.UserService;

public class GetServerInformationCommand extends BaseCommand<String>{

	public GetServerInformationCommand(GameCore gamecore, UserService userService, Message<String> message) {
		super(gamecore, userService, message);
	}

	@Override
	public void execute() {
		int onlineUserAmount = gamecore.getOnlineUsers().size();
		int onlineRoomAmount = gamecore.getRoomList().size();
		
		userService.respond(new Message<>(message.getEvent(), Status.success, 
				new ServerInformation(onlineUserAmount, onlineRoomAmount)));
	}

}
