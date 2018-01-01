package container.waterbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Request;

import com.google.gson.Gson;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import gamecore.model.RequestStatus;
import utils.MyGson;

import static container.Constants.*;
import static container.Constants.Events.*;
import static container.Constants.Events.Signing.*;
import static container.Constants.Events.RoomList.*;
import static container.Constants.Events.Chat.*;
import static container.Constants.Events.InRoom.*;

public class WbotClient implements Client{
	private static final Logger log = LogManager.getLogger(WbotClient.class);
	private static final String LOCAL_IP = "127.0.0.1";
	private static final String SERVER_IP = "35.194.237.84";
	private static final String SELECTED_IP = SERVER_IP;
	private boolean connected = false;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private WaterBot waterBot;
	private ProtocolFactory protocolFactory;
	private Timer timer = new Timer();
	private Gson gson = MyGson.getGson();

	public WbotClient(WaterBot waterBot, ProtocolFactory protocolFactory) {
		this.waterBot = waterBot;
		this.waterBot.setClient(this);
		this.protocolFactory = protocolFactory;
	}

	@Override
	public void run() {
		Socket socket;
		try {
			socket = new Socket(SELECTED_IP, 5278);
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			connected = true;
			log.trace("Robot " + waterBot.getName() + " has setup his socket connection.");
			waterBot.start();
			signInAfterFiveSeconds();
			listeningInput();
		} catch (IOException e) {
			log.error("Error while the robot " + waterBot.getName() + " setup the socket.");
		}	
	}

	private void signInAfterFiveSeconds() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Protocol protocol = protocolFactory.createProtocol(SIGNIN, RequestStatus.request.toString(),
						gson.toJson(new Player(waterBot.getName())));
				broadcast(protocol);
			}
		}, TimeUnit.SECONDS.toMillis(5));
	}
	private void listeningInput(){
		while(connected)
		{
			try {
				String message = dataInputStream.readUTF();
				Protocol protocol = protocolFactory.createProtocol(message);
				waterBot.receive(protocol);
			} catch (IOException e) {
				log.error("Error while the robot " + waterBot.getName() + " listening.");
			}
		}
	}
	
	@Override
	public void broadcast(Protocol protocol) {
		try {
			log.trace("Robot " + waterBot.getName() + " broadcasting: " + protocol);
			dataOutputStream.writeUTF(protocol.toString());
			dataOutputStream.flush();
		} catch (IOException e) {
			log.error("Error while the robot " + waterBot.getName() + " responding.");
		}
	}

	@Override
	public String getId() {
		return String.valueOf(waterBot.getWid());
	}

	@Override
	public String getAddress() {
		return "Robot-address";
	}

	@Override
	public void disconnect() throws Exception {
		connected = false;
		log.trace("Robot " + waterBot.getName() + " disconnected.");
	}
}
