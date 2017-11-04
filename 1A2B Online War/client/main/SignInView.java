package main;

import com.google.gson.Gson;

import Util.Input;
import communication.message.Message;
import communication.message.MessageUtils;
import communication.protocol.Protocol;
import communication.protocol.ProtocolFactory;
import gamecore.entity.user.UserImp;
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
		UserImp user = new UserImp(name);
		
		Gson gson = new Gson();
		String json = gson.toJson(user);
		Protocol protocol = protocolfactory.createProtocol("signIn","request",json);
		
		SocketConnector.getInstance().send(protocol.toString(), this);
	}

	@Override
	public String getViewName() {
		return "Sign-In View";
	}

	@Override
	public void onReceive(String message) {
		Protocol receiveProrocol = protocolfactory.createProtocol(message);
		Message<UserImp> result = MessageUtils.protocolToMessage(receiveProrocol, UserImp.class);
		UserImp signedInUser = result.getData();
		System.out.println("Sign In successfully ! -> User : " + signedInUser);
	}
	
	
	@Override
	public void onDestroy() {
		//TODO �귽����
	}

}
