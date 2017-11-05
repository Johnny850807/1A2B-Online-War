package main;

import com.google.gson.Gson;

import Util.Input;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.Player;
import module.FactoryModule;
import module.SocketConnector;

public class SignInView extends View implements SocketConnector.Callback{
	private final int SIGNIN = 1;
	private final int GETINFO = 2;
	private ProtocolFactory protocolfactory;
	
	@Override
	public void onCreate() {
		SocketConnector.getInstance().connect();
		protocolfactory = FactoryModule.getGameFactory().getProtocolFactory();
	}

	@Override
	public void onRecycleActions() {
		int action = Input.nextInt("(1) Sign In (2) Get Server Info : ", 1, 2);
		switch (action) {
		case SIGNIN:
			signIn();
			break;
		case GETINFO:
			getServerInfo();
			break;
		}
		
	}
	
	private void signIn(){
		String name = Input.next("Input your name: ");
		String json = new Gson().toJson(new Player(name));
		Protocol protocol = protocolfactory.createProtocol("SignIn", "request", json);
		SocketConnector.getInstance().send(protocol.toString(), this, SIGNIN);
	}

	private void getServerInfo(){
		Protocol protocol = protocolfactory.createProtocol("GetServerInformation", "request", null);
		SocketConnector.getInstance().send(protocol.toString(), this, GETINFO);
	}
	
	@Override
	public String getViewName() {
		return "Sign-In View";
	}

	@Override
	public void onReceive(String message, int requestCode) {
		switch (requestCode) {
		case SIGNIN:
			Protocol receiveProrocol = protocolfactory.createProtocol(message);

			System.out.println("Sign In successfully ! -> User : " + receiveProrocol);
			break;
		case GETINFO:
			break;
		}
	}
	
	
	@Override
	public void onDestroy() {
		//TODO ¸ê·½ÄÀ©ñ
	}

}
