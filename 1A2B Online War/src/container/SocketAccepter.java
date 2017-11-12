package container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import container.base.Client;
import container.base.IO;
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
			System.out.println("Client connected: " + clientSocket.getLocalAddress().toString());
			if(clientSocket.isConnected()) 
			{
				IO io = new SocketIO(clientSocket);
				Client userService = gameFactory.createService(io, clientSocket.getInetAddress().toString());
				new Thread(userService).start();
			}
		}
	}
	
	
}
