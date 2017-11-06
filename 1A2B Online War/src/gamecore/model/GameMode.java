package gamecore.model;

import java.io.Serializable;

import gamecore.rooms.games.Game;
import gamecore.rooms.games.GameDuel1A2B;
import gamecore.rooms.games.GameGroup1A2B;
import gamecore.rooms.games.MockGame;

/**
 * GameMode with a minimum/maximum player limit, as well as each GameMode may own its GameCreator which
 * uses a Factory Method for creating the specific Game.
 */
public enum GameMode implements IGameMode , Serializable{
    DUEL1A2B(2,2, new GameCreator(){
		@Override
		public Game createGame() {
			return new GameDuel1A2B();
		}
    }),
    
    GROUP1A2B(2,6, new GameCreator(){
		@Override
		public Game createGame() {
			return new GameGroup1A2B();
		}
    }),
    
    DIXIT(3,6, new GameCreator(){
		@Override
		public Game createGame() {
			return new MockGame();  //TODO DIXIT
		}
    });

    private int minPlayerAmount;
    private int maxPlayerAmount;
    private GameCreator gameCreator;

    GameMode(int minPlayerAmount, int maxPlayerAmount, GameCreator gameCreator) {
        this.minPlayerAmount = minPlayerAmount;
        this.maxPlayerAmount = maxPlayerAmount;
        this.gameCreator = gameCreator;
    }

    @Override
    public int getMinPlayerAmount() {
        return minPlayerAmount;
    }

    public int getMaxPlayerAmount(){
        return maxPlayerAmount;
    }
    
    public Game createGame(){
    	return this.gameCreator.createGame();
    }

    
    public interface GameCreator{
    	Game createGame();
    }
}
