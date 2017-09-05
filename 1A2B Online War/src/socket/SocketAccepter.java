package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import communication.protocol.ProtocolFactory;
import gamefactory.GameFactory;

public class SocketAccepter implements Runnable{
	private GameFactory gameFactory;

	
	public SocketAccepter(GameFactory factory){
		this.gameFactory = factory;
	}


	@Override
	public void run() {
		// TODO 不停偵聽新的 socket 
		int port = 9526;
		
		try {
			ServerSocket server = new ServerSocket(port);
			
			while(true)	{
				Socket clientSocket = server.accept();
				if(clientSocket.isConnected()) {
					
					SocketService clientService = new SocketService(gameFactory,clientSocket.getInputStream(),clientSocket.getOutputStream());
					
					new Thread(clientService).start();
						
					clientSocket.close();
				}
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
}
