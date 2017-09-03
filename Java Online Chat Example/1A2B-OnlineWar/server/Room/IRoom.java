package Room;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Room.Room.Listener;
import User.IUser;

public interface IRoom extends Remote{
	String getName() throws RemoteException ;
	void setName(String name) throws RemoteException;
	void join(IUser user) throws RemoteException;
	void addListener(Listener listener) throws RemoteException;
	void removeListener(Listener listener) throws RemoteException;
}