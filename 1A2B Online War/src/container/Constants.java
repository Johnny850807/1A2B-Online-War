package container;

public final class Constants {
	public final static class Events{
		public final static class Signing{
			public final static String SIGNIN = "SignIn";
			public final static  String SIGNOUT = "SignOut";
			public final static  String GETINFO = "GetServerInformation";
		}
		public final static class RoomList{
			public final static String CREATE_ROOM = "CreateRoom";
			public final static String GET_ROOMS = "GetRooms";
			public final static String JOIN_ROOM = "JoinRoom";
		}
		public final static class InRoom{
			public final static String CLOSE_ROOM = "CloseRoom";
			public final static String LEAVE_ROOM = "LeaveRoom";
			public final static String CHANGE_STATUS = "ChangeStatus";
			public final static String LAUNCH_GAME = "LaunchGame";
		}
		public final static class Chat{
			public final static String SEND_MSG = "SendChatMessage";
		}
		public final static class Games{
			public final static class Duel1A2B{
				public final static String SET_ANSWER = "SetAnswerDuel1A2B";
				public final static String GUESS = "GuessDuel1A2B";
			}
		}
	}
	public interface ErrorCode{
		
	}
	
}