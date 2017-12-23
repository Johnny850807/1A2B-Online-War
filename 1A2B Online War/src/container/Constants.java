package container;

public final class Constants {
	public interface Events{
		public interface Signing{
			String SIGNIN = "SignIn";
			String SIGNOUT = "SignOut";
			String GETINFO = "GetServerInformation";
		}
		public interface RoomList{
			String CREATE_ROOM = "CreateRoom";
			String GET_ROOMS = "GetRooms";
			String JOIN_ROOM = "JoinRoom";
		}
		public interface InRoom{
			String CLOSE_ROOM = "CloseRoom";
			String CHANGE_STATUS = "ChangeStatus";
			String LAUNCH_GAME = "LaunchGame";
		}
		public interface Chat{
			String SEND_MSG = "SendChatMessage";
		}
		public interface Games{
			public interface Duel1A2B{
				String SET_ANSWER = "SetAnswerDuel1A2B";
				String GUESS = "GuessDuel1A2B";
			}
		}
	}
	public interface ErrorCode{
		
	}
	
}
