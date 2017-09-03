package Core;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import static Core.ServerConstant.*;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import Room.IRoom;
import Room.Room;
import User.IUser;
import User.User;

public class ChatCore extends UnicastRemoteObject implements Serializable, IChatCore{
	private static final long serialVersionUID = 1L;
	
	private List<IRoom> roomList = new ArrayList<>();
	private Map<IUser,Listener> listeners = new HashMap<>();
	
	public ChatCore() throws RemoteException {}
	
	@Override
	public IUser signIn(String name) throws RemoteException {
		return new User(name);
	}
	
	@Override
	public void addListener(IUser user, Listener listener) throws RemoteException{
		listeners.put(user, listener);
	}
	
	@Override
	public void removeListener(Listener listener) throws RemoteException{
		listeners.remove(listener);
	}
	
	@Override
	public List<IRoom> getRoomList() throws RemoteException {
		return roomList;
	}
	
	@Override
	public List<IUser> getOnlineUsers() throws RemoteException {
		return new ArrayList<>(listeners.keySet());
	}
	
	@Override
	public IRoom createRoom(String name) throws RemoteException {
		IRoom room = (IRoom) UnicastRemoteObject.exportObject(new Room(name), PORT);
		roomList.add(room);
		notifyAllListeners(l -> l.onNewRoomCreated(room));
		return room;
	}
	
	@Override
	public void closeRoom(IRoom room) throws RemoteException{
		roomList.remove(room);
		notifyAllListeners(l -> l.onRoomClosed(room));
	}
	
	
	private void notifyAllListeners(ListenerAction action){
		for (Listener listener : listeners.values())
			try{
				action.accept(listener);
			}catch (RemoteException err) {
				listeners.remove(listeners.get(listener)); // remove the disconnected client out of the list
			}
	}
	
	public abstract static class Listener extends UnicastRemoteObject implements Remote{
		private static final long serialVersionUID = 1L;
		
		public Listener() throws RemoteException {}
		public abstract void onNewRoomCreated(IRoom room) throws RemoteException ;
		public abstract void onRoomClosed(IRoom room)  throws RemoteException ;
	}
	
	// The function interface which handles action with a remote exception
	public interface ListenerAction{
		void accept(Listener listener) throws RemoteException;
	}
	
}
