import static Core.ServerConstant.*;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import Core.ChatCore;
import Core.IChatCore;

public class ServerMain {
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(PORT);
			IChatCore chatCore = new ChatCore();
			Naming.bind(URI + CORE, chatCore);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

}
