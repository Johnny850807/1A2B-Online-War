package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

import command.base.Command;
import communication.commandparser.base.CommandParser;
import communication.commandparser.base.CommandParserFactory;
import communication.message.Message;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.GameCore;
import gamecore.entity.Entity;
import gamefactory.GameFactory;
import userservice.ServiceIO;
import userservice.UserService;

public class SocketService implements UserService{
	private GameFactory gameFactory;
	private ProtocolFactory protocolFactory;
	private CommandParserFactory commandParserFactory;
	private GameCore gameCore;
	
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;

	public SocketService(GameFactory factory, ServiceIO io) {
		this.gameFactory = factory;
		this.commandParserFactory = gameFactory.getCommandParserFactory();
		this.protocolFactory = gameFactory.getProtocolFactory();
		this.gameCore = gameFactory.getGameCore();
		
		try {
			dataOutput =  new DataOutputStream(io.getOutputStream());
			dataInput =  new DataInputStream(io.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) 
			{
				String content = dataInput.readUTF();
				Protocol protocol = protocolFactory.createProtocol(content);
				CommandParser parser = commandParserFactory.createCommandParser(this);
				System.out.println(protocol);
				Command command = parser.parse(protocol);
				gameCore.executeCommand(command);
			}
		} catch (IOException e) {
			// TODO means this socket close out
		}
	}
	
	@Override
	public void respond(Message<? extends Entity> message) {
		try {
			String event = message.getEvent().toString();
			String status = message.getStatus().toString();
			String jsonData = new Gson().toJson(message.getData());
			Protocol protocol = protocolFactory.createProtocol(event,status,jsonData);
			dataOutput.writeUTF(protocol.toString());
			dataOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void disconnect() throws Exception {
		// TODO ¤¤Â_³s±µ
		
	}

}
