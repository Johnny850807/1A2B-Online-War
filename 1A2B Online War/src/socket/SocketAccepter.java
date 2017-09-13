package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import communication.protocol.ProtocolFactory;
import gamefactory.GameFactory;
import userservice.ServiceIO;
import userservice.UserService;

public class SocketAccepter implements Runnable 
{
	private GameFactory gameFactory;
	private final static int PORT = 5278;
	private ServerSocket server;
	
	public SocketAccepter(GameFactory factory) 
	{
		this.gameFactory = factory;
	}
	
	@Override
	public void run() 
	{
		try {
			server = new ServerSocket(PORT);
			listeningInput();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void listeningInput() throws IOException
	{
		while(true)	
		{
			Socket clientSocket = server.accept();
			if(clientSocket.isConnected()) 
			{
				ServiceIO io = new SocketIO(clientSocket);
				UserService userService = gameFactory.createService(io);
				new Thread(userService).start();
			}
		}
	}
	
	
}
