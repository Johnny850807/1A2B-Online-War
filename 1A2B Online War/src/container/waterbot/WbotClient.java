package container.waterbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;

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
			listeningInput();
		} catch (IOException e) {
			log.error("Error while the robot " + waterBot.getName() + " setup the socket.");
		}	
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
			log.trace("Robot " + waterBot.getName() + " responding: " + protocol);
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
