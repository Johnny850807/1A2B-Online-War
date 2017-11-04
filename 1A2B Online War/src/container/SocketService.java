package container;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamefactory.GameFactory;

public class SocketService implements UserService{
	private GameFactory gameFactory;
	private ProtocolFactory protocolFactory;
	private GameCore gameCore;
	
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;

	public SocketService(GameFactory factory, ServiceIO io) {
		try {
			initProperties(factory, io);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initProperties(GameFactory factory, ServiceIO io) throws Exception{
		this.gameFactory = factory;
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
			e.printStackTrace();
			askGamecoreToUnregisterTheClient();
		}
	}
	
	private void listeningToClientInput() throws IOException{
		while (true) 
		{
			String content = dataInput.readUTF();
			Protocol protocol = protocolFactory.createProtocol(content);
			System.out.println("===== Request ===== \n" + protocol);
			//TODO send the protocol to the event handler
		}
	}
	
	@Override
	public void respond(Protocol protocol) {
		try {
			System.out.println("===== Response ===== \n" + protocol);
			dataOutput.writeUTF(protocol.toString());
			dataOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			askGamecoreToUnregisterTheClient();
		}
	}


	@Override
	public void disconnect() throws Exception {
		askGamecoreToUnregisterTheClient();
	}
	
	private void askGamecoreToUnregisterTheClient(){
		
	}

}
