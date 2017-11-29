package gamecore;

import javax.print.DocFlavor.STRING;

import gamecore.entity.GameRoom;
import gamecore.rooms.games.Game;

public interface GameBinder {
	Game bindGame(GameRoom gameRoom);
	Game getBindedGame(String gameRoomId);
	Game getBindedGame(GameRoom gameRoom);
	void unbindedGame(Game game);
	void unbindedGame(GameRoom gameRoom);
}
