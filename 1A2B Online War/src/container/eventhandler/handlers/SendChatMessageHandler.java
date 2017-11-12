package container.eventhandler.handlers;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;

public class SendChatMessageHandler extends GsonEventHandler<ChatMessage, ChatMessage>{
	private String roomId;
	public SendChatMessageHandler(Client client, Protocol request, GameCore gameCore, ProtocolFactory protocolFactory) {
		super(client, request, gameCore, protocolFactory);
	}

	@Override
	protected Class<ChatMessage> getDataClass() {
		return ChatMessage.class;
	}

	@Override
	protected Response onHandling(ChatMessage chatMessage) {
		chatMessage.initId();
		GameRoom room = gameCore().getGameRoom(chatMessage.getGameRoomId());
		roomId = room.getId();
		room.addChatMessage(chatMessage);
		return success(chatMessage);
	}

	@Override
	protected void onRespondSuccessfulProtocol(Protocol responseProtocol) {
		gameCore().notifyAllClientPlayersInRoom(roomId, responseProtocol);
	}

}
