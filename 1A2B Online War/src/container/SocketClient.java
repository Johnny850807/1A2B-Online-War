package container;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import container.base.Client;
import container.base.IO;
import container.eventhandler.EventHandler;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamecore.entity.Player;
import gamefactory.GameFactory;

public class SocketClient extends Entity implements Client{
	private GameFactory gameFactory;
	private GameEventHandlerFactory gameEventHandlerFactory;
	private ProtocolFactory protocolFactory;
	private GameCore gameCore;
	
	private String address;
	private IO io;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;

	public SocketClient(GameFactory factory, IO io, String address) {
		try {
			this.initId(); 
			this.address = address;
			initProperties(factory, io);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initProperties(GameFactory factory, IO io) throws Exception{
		this.gameFactory = factory;
		this.gameEventHandlerFactory = factory.getGameEventHandlerFactory();
		this.protocolFactory = gameFactory.getProtocolFactory();
		this.gameCore = gameFactory.getGameCore();
		this.dataOutput =  new DataOutputStream(io.getOutputStream());
		this.dataInput =  new DataInputStream(io.getInputStream());
	}

	@Override
	public void run() {
		try {
			listeningToClientInput();
		} catch (IOException e) {
			askGamecoreToUnregisterTheClient();
		}
	}
	
	private void listeningToClientInput() throws IOException{
		while (true) 
		{
			String content = dataInput.readUTF();
			Protocol protocol = protocolFactory.createProtocol(content);
			System.out.println("===== Request ===== \n" + protocol);
			EventHandler handler = gameEventHandlerFactory.createGameEventHandler(this, protocol);
			handler.handle();
		}
	}
	
	@Override
	public void respond(Protocol protocol) {
		try {
			System.out.println("===== Response ===== \n" + protocol);
			dataOutput.writeUTF(protocol.toString());
			dataOutput.flush();
		} catch (IOException e) {
			askGamecoreToUnregisterTheClient();
		}
	}


	@Override
	public void disconnect() throws Exception {
		askGamecoreToUnregisterTheClient();
	}
	
	private void askGamecoreToUnregisterTheClient(){
		try{
			gameCore.removeClientPlayer(getId());
		}catch (IllegalStateException e) {
			System.out.println("Non-Signed In Client disconnects, id: " + getId() + ", address: " + getAddress());
		}
	}

	@Override
	public String getAddress() {
		return address;
	}

}
