package container;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import gamecore.model.GameMode;
import gamecore.rooms.games.Game;
import gamecore.rooms.games.GameDuel1A2B;
import gamecore.rooms.games.GameGroup1A2B;

public final class Domain {
	public interface ErrorCode{
		
	}
	
	public static class GameSimpleFactory{
		static Map<GameMode, Supplier<Game>> gameSupplierMap = new HashMap<>();
		
		static {
			gameSupplierMap.put(GameMode.DUEL1A2B, () -> new GameDuel1A2B(GameMode.DUEL1A2B));
			gameSupplierMap.put(GameMode.GROUP1A2B, () -> new GameGroup1A2B(GameMode.GROUP1A2B));
	    }
		
		public static Game createGame(GameMode gameMode){
			return gameSupplierMap.get(gameMode).get();
		}
	}
}
