package container.waterbot.brain;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import container.waterbot.Brain;
import container.waterbot.WaterBot;
import gamecore.model.ClientStatus;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RoomStatus;

import static container.Constants.Events.InRoom.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;
import static container.Constants.Events.Games.*;

public class GameBrain extends ChainBrain{

	public GameBrain(Brain next, ProtocolFactory protocolFactory) {
		super(next, protocolFactory);
	}

	@Override
	protected void onReceiveSuccessProtocol(WaterBot waterBot, Protocol protocol, Client client) {
		switch (protocol.getEvent()) {
		case LAUNCH_GAME:
			enterToTheGame(waterBot, client);
			break;
		case GAMEOVER:
			waterBot.getMe().setUserStatus(ClientStatus.signedIn);
			waterBot.clearGameRoom();
			break;
		default:
			break;
		}
		nextIfNotNull(waterBot, protocol, client);
	}
	
	private void enterToTheGame(WaterBot waterBot, Client client) {
		PlayerRoomIdModel model = new PlayerRoomIdModel(waterBot.getMe().getId(), waterBot.getGameRoom().getId());
		Protocol protocol = protocolFactory.createProtocol(ENTERGAME, REQUEST, gson.toJson(model));
		client.broadcast(protocol);
	}

}
