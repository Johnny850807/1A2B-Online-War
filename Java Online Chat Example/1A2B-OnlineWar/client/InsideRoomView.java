import java.rmi.RemoteException;

import Room.IRoom;
import Room.Room;
import User.IUser;
import Util.Input;
import View.View;

public class InsideRoomView extends View{
	private static final long serialVersionUID = 1L;
	
	private IUser user;
	private IRoom room;
	
	public InsideRoomView(IUser user, IRoom room) throws RemoteException {
		this.user = user;
		this.room = room;
	}
	
	@Override
	protected void onCreate() {
		try{
			room.addListener(Listener);
			
		}catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void runChatting(){
		while(isAlive())
		{
			String message = Input.nextLine("Input message (input 'exit' for leaving)");
			if (message.toLowerCase().contains("exit"))
				finish();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
	}
	
	private Room.Listener Listener = new Room.Listener() {

		@Override
		public void onReceiveMessage(String message) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRoomClosed() {
			// TODO Auto-generated method stub
			
		}
	};

}
