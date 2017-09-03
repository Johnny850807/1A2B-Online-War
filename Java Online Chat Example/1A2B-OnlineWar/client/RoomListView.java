import java.rmi.RemoteException;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

import Core.ChatCore;
import Core.IChatCore;
import Core.ChatCore.Listener;
import Room.IRoom;
import User.IUser;
import Util.Input;
import View.View;

public class RoomListView extends View{
	private static final long serialVersionUID = 1L;
	
	private List<IRoom> roomList;
	private IChatCore chatCore;
	private IUser user;
	
	public RoomListView(IChatCore chatCore, IUser user) throws RemoteException {
		this.chatCore = chatCore;
		this.user = user;
		roomList = chatCore.getRoomList();
	}
	
	@Override
	protected void onCreate() {
		try{
			chatCore.addListener(user, listener);
			printRoomList();
			runActions();
		}catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
	}
	
	private void runActions(){
		while(isAlive())
		{
			int action = Input.nextInt("(1) Create room (2) Print room list (3) Join Room : ",1,3);
			switch (action) {
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
	
				break;
			default:
				throw new IllegalStateException("Not supported action " + action);
			}
		}
	}
	
	private void createRoom() throws RemoteException{
		String name = Input.next("Room name: ");
		chatCore.createRoom(name);
	}
	
	private void printRoomList() throws RemoteException{
		for (int i = 0 ; i < roomList.size() ; i ++ )
			System.out.println("(" + i + ") " + roomList.get(i).getName());
	}
	
	private void joinRoom() throws RemoteException{
		int number = Input.nextInt("Room number: ", 0, roomList.size());
		IRoom room = roomList.get(number);
		room.join(user);
	}
	
	private ChatCore.Listener listener = new Listener() {
		
		@Override
		public void onRoomClosed(IRoom room) throws RemoteException {
			System.out.println("Room " + room.getName() + " closed.");
			roomList.remove(room);
		}
		
		@Override
		public void onNewRoomCreated(IRoom room) throws RemoteException {
			roomList.add(room);
			System.out.println("New room created : " + room.getName());
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			chatCore.removeListener(listener);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
