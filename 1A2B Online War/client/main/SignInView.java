package main;

import com.google.gson.Gson;

import Util.Input;
import container.protocol.Protocol;
import container.protocol.ProtocolFactory;
import gamecore.entity.User;
import module.FactoryModule;
import module.SocketConnector;

public class SignInView extends View implements SocketConnector.Callback{
	private ProtocolFactory protocolfactory;
	
	@Override
	public void onCreate() {
		SocketConnector.getInstance().connect();
		protocolfactory = FactoryModule.getGameFactory().getProtocolFactory();
	}

	@Override
	public void onRecycleActions() {
		String name = Input.next("Input your name: ");
		User user = new User(name); 
		
		Gson gson = new Gson();
		String json = gson.toJson(user);
		Protocol protocol = protocolfactory.createProtocol("SignIn", "request", json);
		
		SocketConnector.getInstance().send(protocol.toString(), this);
	}

	@Override
	public String getViewName() {
		return "Sign-In View";
	}

	@Override
	public void onReceive(String message) {
		Protocol receiveProrocol = protocolfactory.createProtocol(message);

		System.out.println("Sign In successfully ! -> User : " + receiveProrocol);
	}
	
	
	@Override
	public void onDestroy() {
		//TODO ¸ê·½ÄÀ©ñ
	}

}
