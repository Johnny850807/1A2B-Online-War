package gamecore;

import java.util.HashMap;
import java.util.Map;

import container.Domain;
import gamecore.entity.GameRoom;
import gamecore.model.GameMode;
import gamecore.model.RoomStatus;
import gamecore.rooms.games.Game;

public class ReleasesGameBinder implements GameBinder{
	private Map<String, Game> gameMap = new HashMap<>();  // <gameRoom id, game>
	
	@Override
	public Game bindGame(GameRoom gameRoom) {
		String id = gameRoom.getId();
		if (id == null)
			throw new NullPointerException("Binded gameRoom's id should not be null.");
		GameMode gameMode = gameRoom.getGameMode();
		Game launchedGame = Domain.GameSimpleFactory.createGame(gameMode);
		launchedGame.setGameRoom(gameRoom);
		gameMap.put(id, launchedGame);
		return launchedGame;
	}

	@Override
	public Game getBindedGame(GameRoom gameRoom) {
		return getBindedGame(gameRoom.getId());
	}

	@Override
	public Game getBindedGame(String gameRoomId) {
		return gameMap.get(gameRoomId);
	}
	
	@Override
	public void unbindedGame(Game game) {
		gameMap.remove(game.getGameId());
	}

	@Override
	public void unbindedGame(GameRoom gameRoom) {
		gameMap.remove(gameRoom.getId());
	}


}
