import static Core.ServerConstant.CORE;
import static Core.ServerConstant.URI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Core.IChatCore;
import User.IUser;
import Util.Input;

public class ClientMain {
	private static IChatCore chatCore;
	public static void main(String[] args) {
		try {
			chatCore = (IChatCore) Naming.lookup(URI + CORE);
			run();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void run() throws RemoteException{
		while(true){
			String name = Input.next("¿é¤J¼ÊºÙ¡G");
			IUser user =  chatCore.signIn(name);
			
			System.out.println(user.getName());
			System.out.println(chatCore.getOnlineUsers().size());
		}
	}

}
