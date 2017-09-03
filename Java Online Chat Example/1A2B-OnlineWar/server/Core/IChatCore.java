package Core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Core.ChatCore.Listener;
import Room.IRoom;
import User.IUser;

public interface IChatCore extends Remote{

	IUser signIn(String name) throws RemoteException;

	void addListener(IUser user, Listener listener) throws RemoteException;

	List<IRoom> getRoomList() throws RemoteException;

	List<IUser> getOnlineUsers() throws RemoteException;

	IRoom createRoom(String name) throws RemoteException;

	void closeRoom(IRoom room) throws RemoteException;

	void removeListener(Listener listener) throws RemoteException;

}