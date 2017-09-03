package User;

import java.rmi.RemoteException;
import java.util.UUID;

import Core.Entity;

public class User extends Entity implements IUser{
	private String name;
	
	public User(String name) throws RemoteException {
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
	
	
}
