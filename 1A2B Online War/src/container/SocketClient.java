package container;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.base.Client;
import container.base.IO;
import container.eventhandler.EventHandler;
import container.eventhandler.GameEventHandlerFactory;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamefactory.GameFactory;

public class SocketClient extends Entity implements Client{
	private static transient Logger log = LogManager.getLogger(SocketClient.class);
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
			log.trace("Socket " + getAddress() + " initialized with id: " + getId());
		} catch (Exception e) {
			log.error("err", e);
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
		} catch (RuntimeException e) {
			log.error("Client input runtime error...", e);
		} catch (IOException e) {
			askGamecoreToUnregisterTheClient();
		}
	}
	
	private void listeningToClientInput() throws IOException{
		while (true) 
		{
			String content = dataInput.readUTF();
			Protocol protocol = protocolFactory.createProtocol(content);
			log.info("Request: " + protocol);
			EventHandler handler = gameEventHandlerFactory.createGameEventHandler(this, protocol);
			handler.handle();
		}
	}
	
	@Override
	public void broadcast(Protocol protocol) {
		try {
			log.info("Broadcast to service(" + getAddress() +"): " + protocol);
			dataOutput.writeUTF(protocol.toString());
			dataOutput.flush();
		}catch (IOException e) {
			log.trace("socket " + getAddress() + " disconnected.");
		}catch (Exception e) {
			log.error("err", e);
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
			log.warn("Non-Signed In Client disconnects, id: " + getId() + ", address: " + getAddress());
		}catch (Exception e) {
			log.error("err", e);
		}
	}

	@Override
	public String getAddress() {
		return address;
	}

}
