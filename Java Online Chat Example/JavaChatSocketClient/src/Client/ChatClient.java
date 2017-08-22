package Client;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Closeable {
	public final static String IP = "52.15.121.194";
	public final static int PORT = 5679;
	private Socket clientSocket;
	private DataInputStream input;
	private DataOutputStream output;
	private Scanner console = new Scanner(System.in);
	private boolean running = true;
	private String name;
	private OnClientListener receiveListener;
	
	public void runClient() throws UnknownHostException, IOException{
		clientSocket = new Socket(IP,PORT);
		input = new DataInputStream(clientSocket.getInputStream());
		output = new DataOutputStream(clientSocket.getOutputStream());
		inputProfile();
		receiveListening();
	}
	
	private void inputProfile() throws IOException {
		System.out.println("Input your name: ");
		name = receiveListener.onNaming();
		output.writeUTF(name + " in.");
	}

	private void receiveListening(){
		new Thread(){
			@Override
			public void run() {
				while(running)
					try {
						receiveListener.onReceive(input.readUTF());
					}catch (IOException e) {
						e.printStackTrace();
						close();
					}
			}
		}.start();
	}
	
	public void sendMessage(String message) throws IOException{
		output.writeUTF(name + ": " + message);
		output.flush();
	}
	
	@Override
	public void close() {
		running = false;
	}

	public void setReceiveListener(OnClientListener receiveListener) {
		this.receiveListener = receiveListener;
	}

}
