import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.google.gson.Gson;

import container.Constants;
import container.Constants.Events.Games;
import container.Constants.Events.Games.Duel1A2B;
import container.Constants.Events.Signing;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import module.FactoryModule;
import module.SocketConnector;
import module.SocketConnector.Callback;
import utils.RandomString;

import static container.Constants.Events.RoomList.*;

public class CreateTenRooms {
	public static void main(String[] argv){
		Gson gson = new Gson();
		SocketConnector cn = SocketConnector.getInstance();
		cn.connect();
		ProtocolFactory factory = FactoryModule.getGameFactory().getProtocolFactory();

		Player host = new Player("TEST");
		Protocol signInPtc = factory.createProtocol(Signing.SIGNIN, RequestStatus.request.toString(),
				gson.toJson(host));
		cn.send(signInPtc.toString(), new Callback() {
			@Override
			public void onReceive(String message, int requestCode) {
				Player responseHost = gson.fromJson(factory.createProtocol(message).getData(), Player.class);
				IntStream.range(0, 10).parallel().forEach(i -> {
					host.initId();
					Protocol protocol = factory.createProtocol(CREATE_ROOM, RequestStatus.request.toString(), 
							gson.toJson(new GameRoom(GameMode.DUEL1A2B, RandomString.next(6), responseHost)));
					cn.send(protocol.toString(), new SocketConnector.Callback() {
										@Override
										public void onReceive(String message, int requestCode) {
											System.out.println(message);
										}
									}, i);
				});
			}
		}, 100);
		
	}
}
