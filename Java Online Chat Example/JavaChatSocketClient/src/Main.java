import java.util.Scanner;

import Client.ChatClient;
import Client.OnClientListener;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ChatClient chat = new ChatClient();
		try{
			chat.setReceiveListener(new OnClientListener(){
				@Override
				public void onReceive(String message) {
					System.out.println(message);
				}
		
				@Override
				public String onNaming() {
					return scanner.nextLine();
				}
			});
			chat.runClient();
			while(true)
				chat.sendMessage(scanner.nextLine());
		}catch(Exception err){
			err.printStackTrace();
		}
	}
}
