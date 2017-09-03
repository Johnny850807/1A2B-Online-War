package Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import Core.Entity;
import User.IUser;

public class Room extends Entity implements IRoom{
	private List<IUser> users = new ArrayList<>();
	private List<Listener> listeners = new ArrayList<>();
	private String name;
	
	public Room(String name) throws RemoteException{
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.name = name;
	}

	@Override
	public void join(IUser user) throws RemoteException {
		users.add(user);
	}
	
	@Override
	public void addListener(Listener listener) throws RemoteException {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(Listener listener) throws RemoteException {
		listeners.remove(listener);
	}
	
	public static abstract class Listener extends UnicastRemoteObject implements Remote{
		private static final long serialVersionUID = 1L;
		
		public Listener() throws RemoteException {}
		
		public abstract void onReceiveMessage(String message);
		public abstract void onRoomClosed();
	}
}
