package scripts;
import static container.Constants.Events.RoomList.*;

import java.util.List;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import container.Constants.Events.RoomList;
import container.Constants.Events.Signing;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.PlayerRoomIdModel;
import gamecore.model.RequestStatus;
import module.FactoryModule;
import module.SocketConnector;
import module.SocketConnector.Callback;
import utils.MyGson;
import utils.RandomString;

public class JoinAllRooms {
	public static void main(String[] argv){
		Gson gson = MyGson.getGson();
		SocketConnector cn = SocketConnector.getInstance();
		cn.connect();
		
		//get all rooms 
		ProtocolFactory factory = FactoryModule.getGameFactory().getProtocolFactory();
		Protocol showRoomsPrc = factory.createProtocol(RoomList.GET_ROOMS, RequestStatus.request.toString(),
				null);
		cn.send(showRoomsPrc.toString(), new Callback() {
			@Override
			public void onReceive(String message, int requestCode) {
				//rooms got
				List<GameRoom> rooms = gson.fromJson(factory.createProtocol(message).getData(), 
						new TypeToken<List<GameRoom>>(){}.getType());
				
				//now sign in
				Player player = new Player("PLAYER");
				Protocol signInPtc = factory.createProtocol(Signing.SIGNIN, RequestStatus.request.toString(),
						gson.toJson(player));
				cn.send(signInPtc.toString(), new Callback() {
					@Override
					public void onReceive(String message, int requestCode) {
						//signed in
						Player responsePlayer = gson.fromJson(factory.createProtocol(message).getData(), Player.class);
						
						//now join to all rooms
						rooms.parallelStream().forEach(r -> {
							Protocol protocol = factory.createProtocol(JOIN_ROOM, RequestStatus.request.toString(), 
									gson.toJson(new PlayerRoomIdModel(responsePlayer.getId(), r.getId())));
							cn.send(protocol.toString(), new SocketConnector.Callback() {
								@Override
								public void onReceive(String message, int requestCode) {
									System.out.println(message);
								}
							}, 300);
						});
					}
				}, 100);
			}
		}, 200);
		
		
	}
}
