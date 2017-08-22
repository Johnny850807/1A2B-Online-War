import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer implements Closeable , OnReceiveMessageListener{
	public final static int LISTEN_PORT = 5679;
	public static ChatServer instance = new ChatServer();
	private ServerSocket serverSocket;
	private List<Chatter> chatters = Collections.checkedList(new ArrayList<Chatter>(), Chatter.class);
	
	public static ChatServer getInstance(){
		return instance;
	}
	
	public void runServer() throws IOException{
		serverSocket = new ServerSocket( LISTEN_PORT );
		while(true)
			listenClient();
	}
	
	private void listenClient() throws IOException{
		Socket socket = serverSocket.accept();
		System.out.println("One Socket Enter : " + socket.getRemoteSocketAddress());
		Chatter newChatter = new Chatter(socket);
		chatters.add(newChatter);
		newChatter.receiveListening(this);
	}

	@Override
	public void close() throws IOException {
		serverSocket.close();
	}

	@Override
	public void onReceiveMessage(String message) {
		System.out.println("Message : " + message);
		sendMessageToAll(message);
	}
	
	public void deleteChatter(Chatter chatter){
		chatters.remove(chatter);
	}
	
	private void sendMessageToAll(String message){
		for ( Chatter chatter : chatters )
			chatter.receiveMessage(message);
	}
}
