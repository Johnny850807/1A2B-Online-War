package container;

public final class Constants {
	public final static int VERSION = 1;
	public final static class Events{
		public final static String PING = "Ping";
		public final static String RECONNECTED = "Reconnected";
		public final static class Signing{
			public final static String SIGNIN = "SignIn";
			public final static  String SIGNOUT = "SignOut";
			public final static  String SIGNOUT_TIME_EXPIRED = "SignOutTimeExpired";
			public final static  String GETINFO = "GetServerInformation";
		}
		public final static class RoomList{
			public final static String CREATE_ROOM = "CreateRoom";
			public final static String GET_ROOMS = "GetRooms";
			public final static String JOIN_ROOM = "JoinRoom";
		}
		public final static class InRoom{
			public final static String CLOSE_ROOM = "CloseRoom";
			public final static String CLOSE_ROOM_TIME_EXPIRED = "CloseRoomTimeExpired";
			public final static String LEAVE_ROOM = "LeaveRoom";
			public final static String BOOTED = "Booted";
			public final static String CHANGE_STATUS = "ChangeStatus";
			public final static String LAUNCH_GAME = "LaunchGame";
		}
		public final static class Chat{
			public final static String SEND_MSG = "SendChatMessage";
		}
		public final static class Games{
			public final static String GAMESTARTED = "GameStarted";
			public final static String GAMEINTERRUPTED = "GameInterruptd";
			public final static String ENTERGAME = "EnterGame";
			public final static String GAMEOVER = "GameOver";
			
			public final static class Duel1A2B{
				public final static String SET_ANSWER = "SetAnswerDuel1A2B";
				public final static String GUESS = "GuessDuel1A2B";
				public final static String GUESSING_STARTED = "GuessingStartedDuel1A2B";
				public final static String ONE_ROUND_OVER = "OneRoundOverDuel1A2B";
			}
			
			public final static class Boss1A2B{
				public final static String NEXT_TURN = "NextTurnBoss1A2B";
			}
		}
	}
	public interface ErrorCode{
		
	}
	
}
