package gamecore.model.gamemodels.a1b2;

import java.util.HashMap;
import java.util.Map;

import gamecore.entity.Player;
import gamecore.model.gamemodels.GameModel;

public class Duel1A2BModel extends GameModel{
	private Map<Player, Duel1A2BPlayerBarModel> playerModels = new HashMap<>();
	
	public Duel1A2BModel(Player host, Player anotherPlayer){
		playerModels.put(host, new Duel1A2BPlayerBarModel());
		playerModels.put(anotherPlayer, new Duel1A2BPlayerBarModel());
	}
	
	public void configPlayerAnswer(Player player, String answer) throws NumberNotValidException{
		playerModels.get(player).setAnswer(answer);
	}
	
	public void guess(Player player, String guess) throws NumberNotValidException{
		Duel1A2BPlayerBarModel anotherModel = playerModels.get(getAnotherPlayer(player));
		anotherModel.guess(guess);
	}
	
	/**
	 * @param player the being player
	 * @return the another player from the being player
	 */
	public Player getAnotherPlayer(Player beingPlayer){
		for (Player player : playerModels.keySet())
			if (!player.equals(beingPlayer))
				return player;
		throw new IllegalStateException();
	}
	

}
