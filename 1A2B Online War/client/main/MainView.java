package main;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Util.Input;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.RequestStatus;
import module.FactoryModule;
import module.SocketConnector;

public class MainView extends View implements SocketConnector.Callback{
	private final int SIGNIN = 1;
	private final int GETINFO = 2;
	private final int GETROOM = 3;
	private final int CREATEROOM = 4;
	private final int SIGNOUT = 5;
	private Gson gson = new Gson();
	private ProtocolFactory protocolfactory;
	private Scanner scanner = new Scanner(System.in);
	private Player player;
	
	@Override
	public void onCreate() {
		SocketConnector.getInstance().connect();
		protocolfactory = FactoryModule.getGameFactory().getProtocolFactory();
	}

	@Override
	public void onRecycleActions() {
		int action = Input.nextInt("(1) Sign In (2) Get Server Info (3) Show Rooms (4) Create Room: ", 1, 5);
		switch (action) {
		case SIGNIN:
			signIn();
			break;
		case GETINFO:
			getServerInfo();
			break;
		case GETROOM:
			Protocol getroom = protocolfactory.createProtocol("GetRooms", RequestStatus.request.toString(), null);
			SocketConnector.getInstance().send(getroom.toString(), this, GETROOM);
			break;
		case CREATEROOM:
			if (player == null)
				System.out.println("Sign In First.");
			else 
			{
				Protocol createroom = protocolfactory.createProtocol("CreateRoom", RequestStatus.request.toString(), gson.toJson(createRoom()));
				SocketConnector.getInstance().send(createroom.toString(), this, CREATEROOM);
			}
			break;
		case SIGNOUT:
			finish();
			break;

		}
	}
	
	private void signIn(){
		System.out.println("Name: ");
		String name = scanner.next();
		String json = new Gson().toJson(new Player(name));
		Protocol protocol = protocolfactory.createProtocol("SignIn", "request", json);
		SocketConnector.getInstance().send(protocol.toString(), this, SIGNIN);
	}

	private void getServerInfo(){
		Protocol protocol = protocolfactory.createProtocol("GetServerInformation", "request", null);
		SocketConnector.getInstance().send(protocol.toString(), this, GETINFO);
	}
	
	private GameRoom createRoom(){
		System.out.println("Room name: ");
		String name = scanner.next();
		System.out.println("Game Mode: (1) Duel (2) Group : ");
		int mode = scanner.nextInt();
		GameMode gameMode = mode == 1 ? GameMode.DUEL1A2B : GameMode.GROUP1A2B;
		GameRoom room = new GameRoom(gameMode, name, player);
		return room;
	}
	
	private void showAllRoomsFromResponse(Protocol protocol){
		if (RequestStatus.valueOf(protocol.getStatus()) != RequestStatus.success)
			throw new RuntimeException(protocol.getEvent() + " failed");
		Type type = new TypeToken<List<GameRoom>>(){}.getType();
		List<GameRoom> rooms = gson.fromJson(protocol.getData(), type);
		for (GameRoom room :rooms)
			System.out.println(room);
	}
	
	@Override
	public String getViewName() {
		return "Sign-In View";
	}

	@Override
	public void onReceive(String message, int requestCode) {
		Protocol protocol = protocolfactory.createProtocol(message);
		switch (requestCode) {
		case SIGNIN:
			player = new Gson().fromJson(protocol.getData(), Player.class);
			break;
		case GETINFO:
			break;
		case GETROOM:
			showAllRoomsFromResponse(protocol);
			break;
		case CREATEROOM:
			GameRoom createdRoom = gson.fromJson(protocol.getData(), GameRoom.class);
			System.out.println("Created Room: " + createdRoom);
			break;
		}
	}
	
	
	@Override
	public void onDestroy() {
		//TODO ¸ê·½ÄÀ©ñ
	}

}
