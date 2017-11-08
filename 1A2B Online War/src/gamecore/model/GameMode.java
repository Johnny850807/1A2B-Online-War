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
    DUEL1A2B(2,2),
    
    GROUP1A2B(2,6),
    
    DIXIT(3,6);

    private int minPlayerAmount;
    private int maxPlayerAmount;

    GameMode(int minPlayerAmount, int maxPlayerAmount) {
        this.minPlayerAmount = minPlayerAmount;
        this.maxPlayerAmount = maxPlayerAmount;
    }

    @Override
    public int getMinPlayerAmount() {
        return minPlayerAmount;
    }

    public int getMaxPlayerAmount(){
        return maxPlayerAmount;
    }
    
}
