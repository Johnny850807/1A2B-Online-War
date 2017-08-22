import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Chatter implements Closeable{
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private boolean running = true;
	
	public Chatter(Socket socket) throws IOException{
		this.socket = socket;
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	public void receiveListening(OnReceiveMessageListener listener){
		new Thread(){
			@Override
			public void run(){
				try {
					while(running)
						listener.onReceiveMessage(input.readUTF());
				} catch (IOException e) {
					close();
				}
			}
		}.start();
	}
	
	public void receiveMessage(String message){
		try {
			output.writeUTF(message);
			output.flush();
		} catch (IOException e) {
			close();
		}
	}

	@Override
	public void close(){
		running = false;
		ChatServer.getInstance().deleteChatter(this);
	}
	
}
