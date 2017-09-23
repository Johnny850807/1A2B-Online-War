package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

import com.google.gson.Gson;

import Util.Input;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.entity.user.UserImp;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class SignInView extends View {
	
	private OutputStream output;
	private GameFactory gamefactory;
	private ProtocolFactory protocolfactory;
	
	@Override
	public void onCreate() {
		gamefactory = new GameOnlineReleaseFactory();
		protocolfactory = gamefactory.getProtocolFactory();
		try{
			Socket s = new Socket("35.194.206.10",5278);
			output = s.getOutputStream();
		}catch (ConnectException e) {
			System.out.println("Error: " + e.getMessage());
			System.exit(0);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onRecycleActions() {
		//TODO 登入測試
		String name = Input.next("Input your name: ");
		UserImp user = new UserImp(name);
		
		Gson gson = new Gson();
		String json = gson.toJson(user);
		Protocol protocol = protocolfactory.createProtocol("signIn","request",json);
		
		DataOutputStream d = new DataOutputStream(output);
		
		try {
			d.writeUTF(protocol.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

	@Override
	public void onDestroy() {
		//TODO 資源釋放
	}

	@Override
	public String getViewName() {
		return "Sign-In View";
	}
	
}
