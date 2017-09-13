package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gson.Gson;

import Util.Input;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.entity.user.User;
import gamecore.entity.user.UserImp;
import gamefactory.GameFactory;
import gamefactory.GameOnlineReleaseFactory;

public class SignInView extends View {
	
	private OutputStream output;
	private GameFactory gamefactory;
	private ProtocolFactory protocolfactory;
	
	@Override
	public void onCreate() {
		//TODO 資源生成
		gamefactory = new GameOnlineReleaseFactory();
		protocolfactory = gamefactory.createProtocolFactory();
		try{
			Socket s = new Socket("127.0.0.1",5278);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onRecycleActions() {
		//TODO 登入測試
		String name = Input.next("Input your name: ");
<<<<<<< HEAD
		UserImp user = new UserImp(name);
		
		Gson gson = new Gson();
		String json = gson.toJson(user);
		Protocol protocol = protocolfactory.createProtocol("signIn","request",json);
		System.out.println(protocol);
=======
		if (name.contains("go"))
			new SignInView2().start();
>>>>>>> 22924f02c683cdb8d2556d2d297f6575d1c8b426
		
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
