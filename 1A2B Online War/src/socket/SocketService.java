package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import command.Command;
import communication.commandparser.CommandParser;
import communication.commandparser.CommandParserFactory;
import communication.message.Message;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.entity.Entity;
import gamefactory.GameFactory;

public class SocketService implements UserService{
	private GameFactory gameFactory;
	private ProtocolFactory protocolFactory;
	private CommandParserFactory commandParserFactory;
	
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;

	public SocketService(GameFactory factory, InputStream input, OutputStream output) {
		this.gameFactory = factory;
		this.protocolFactory = gameFactory.createProtocolFactory();
		
		dataOutput =  new DataOutputStream(output);
		dataInput =  new DataInputStream(input);

	}

	@Override
	public void run() {
		while(true)	
		{
			try {
				String content = dataInput.readUTF();
				Protocol protocol = protocolFactory.createProtocol(content);
				Command command = commandParserFactory.createCommandParser(this).parse(protocol);
				gameFactory.createGameCore().executeCommand(command);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void respond(Message<? extends Entity> message) {
		// TODO 回應訊息給 client
		String event = message.getEvent().toString();
		String status = message.getStatus().toString();
		String data = message.getData().toString();
		protocolFactory.createProtocol(event,status,data);
	}


	@Override
	public void disconnect() throws Exception {
		// TODO 中斷連接
		
	}

}
