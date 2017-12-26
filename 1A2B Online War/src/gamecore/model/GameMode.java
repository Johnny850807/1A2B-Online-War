package gamecore.model;

/**
 * GameMode with a minimum/maximum player limit, as well as each GameMode may own its GameCreator which
 * uses a Factory Method for creating the specific Game.
 */
public enum GameMode implements IGameMode{
    DUEL1A2B(2,2),
    
    GROUP1A2B(2,6),
    
    DIXIT(3,6),

	BOSS1A2B(1, 4);
	
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
