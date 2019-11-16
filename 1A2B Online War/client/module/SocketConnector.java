package module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnector {
	private static final String LOCAL_IP = "127.0.0.1";
	private static final String SERVER_IP = "35.231.15.22";
	private static final String SELECTED_IP = LOCAL_IP;
	private static SocketConnector instance = new SocketConnector();
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Callback callback;
	private int requestCode;
	
	public static SocketConnector getInstance(){
		return instance;
	}
	
	public void connect(){
		Socket socket;
		try {
			socket = new Socket(SELECTED_IP, 5278);
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			runListener();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void runListener(){
		new Thread(){
			@Override
			public void run() {
				while(true)
				{
					try {
						String message = dataInputStream.readUTF();
						System.out.println("Receive : " + message);
						callback.onReceive(message, requestCode);
						requestCode = 1000; //reset
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void send(String message, Callback callback, int requestCode){
		try {
			this.callback = callback;
			this.requestCode = requestCode;
			dataOutputStream.writeUTF(message);
			dataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public interface Callback{
		void onReceive(String message, int requestCode);
	}
}
