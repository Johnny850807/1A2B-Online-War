package container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import container.protocol.ProtocolFactory;
import gamefactory.GameFactory;

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
				Client userService = gameFactory.createService(io);
				new Thread(userService).start();
			}
		}
	}
	
	
}
