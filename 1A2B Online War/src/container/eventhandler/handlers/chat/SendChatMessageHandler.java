package container.eventhandler.handlers.chat;

import container.base.Client;
import container.eventhandler.handlers.GsonEventHandler;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.ChatMessage;
import gamecore.entity.GameRoom;

/**
 * @author Johnny850807
 * Input: the content of the chat message.
 * Output: the message with its unique id.
 */
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
		gameCore().broadcastRoom(roomId, responseProtocol);
	}

}
