package scripts;


import com.google.gson.Gson;

import container.Constants.Events.Games;
import container.Constants.Events.Games.Duel1A2B;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.model.ContentModel;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RequestStatus;
import module.FactoryModule;
import module.SocketConnector;
import utils.MyGson;

/**
 * a script used to run any protocol.
 */
public class RunProtocol {
	private static final String PLAYERID = "a69b02fc-3b0f-421d-b4ca-200a0ce765fa";
	private static final String ROOMID = "a69b02fc-3b0f-421d-b4ca-200a0ce765fas";
	private static final String ANSWER = "5678";
	private static final String GUESSNUMBER = "1234";
	private static final Gson gson = MyGson.getGson();
	private static final ProtocolFactory factory = FactoryModule.getGameFactory().getProtocolFactory();
	private static final Protocol ENTERGAME = factory.createProtocol(Games.ENTERGAME, RequestStatus.request.toString(), 
			gson.toJson(new PlayerRoomIdModel(PLAYERID, ROOMID)));
	private static final Protocol SET_ANSWER = factory.createProtocol(Duel1A2B.SET_ANSWER, RequestStatus.request.toString(), 
			gson.toJson(new ContentModel(PLAYERID, ROOMID, ANSWER)));
	private static final Protocol GUESS = factory.createProtocol(Duel1A2B.GUESS, RequestStatus.request.toString(), 
			gson.toJson(new ContentModel(PLAYERID, ROOMID, GUESSNUMBER)));
	private static final Protocol TARGET = ENTERGAME;

	public static void main(String[] argv){
		SocketConnector cn = SocketConnector.getInstance();
		cn.connect();
		
		cn.send(TARGET.toString(), new SocketConnector.Callback() {
			@Override
			public void onReceive(String message, int requestCode) {
				System.out.println(message);
			}
		}, 200);
	}
}
