package User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUser extends Remote{
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;
}