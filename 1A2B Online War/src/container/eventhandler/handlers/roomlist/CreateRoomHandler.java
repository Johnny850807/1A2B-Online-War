package container.eventhandler.handlers.roomlist;

import container.ApacheLoggerAdapter;
import container.base.Client;

import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientStatus;

/**
 * @author Johnny850807
 * Input: the creating room.
 * Output: (RoomList) the created room.
 */
public class CreateRoomHandler extends GsonEventHandler<GameRoom, GameRoom>{

	public CreateRoomHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}
	
	@Override
	protected Class<GameRoom> getDataClass() {
		return GameRoom.class;
	}

	@Override
	protected Response onHandling(GameRoom room) {
		try{
			validateRoom(room);
			validateHost(room.getHost());
			Player hostPlayer = gameCore().getClientPlayer(room.getHost().getId()).getPlayer();
			room.initId();
			room.setLog(new ApacheLoggerAdapter(GameRoom.class));
			room.setHost(hostPlayer);
			room.setProtocolFactory(protocolFactory());
			gameCore().addGameRoom(room);
			return success(room);
		}catch (NullPointerException e) {
			return error(404, e);
		}catch (IllegalArgumentException e) {
			return error(400, e);
		}catch (IllegalAccessException e) {
			return error(403, e);
		}
	}

	private void validateRoom(GameRoom room){
		if (room.getName() == null || room.getName().isEmpty() || room.getName().length() > 9)
			throw new IllegalArgumentException("The room's name should be in the range(1~9)");
		if (room.getHost() == null)
			throw new IllegalArgumentException("The room should be hosted by a player.");
		if (room.getGameMode() == null)
			throw new IllegalArgumentException("The room should be given a game mode.");
	}
	
	private void validateHost(Player host) throws IllegalAccessException{
		if (!host.getId().equals(client().getId()))
			throw new IllegalAccessException("You are not the host, how did you send the request?");
		if (hasThePlayerJoinedAnotherRoom(host))
			throw new IllegalArgumentException("The player has joined to another room.");
	}
	
	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		//the gamecore has handled it.
	}

}
