package gamecore.model.gamemodels.a1b2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamecore.entity.Player;
import gamecore.model.gamemodels.GameModel;

/**
 * @author Waterball
 * the model holding all the instances of one 1A2B-duel game.
 */
public class Duel1A2BModel extends GameModel{
	private Map<Player, Duel1A2BPlayerBarModel> playerModels = new HashMap<>();
	private Player winner = null;
	
	public Duel1A2BModel(Player host, Player anotherPlayer){
		playerModels.put(host, new Duel1A2BPlayerBarModel(host.getName()));
		playerModels.put(anotherPlayer, new Duel1A2BPlayerBarModel(anotherPlayer.getName()));
	}
	
	public void commitPlayerAnswer(Player player, String answer) throws NumberNotValidException{
		playerModels.get(player).setAnswer(answer);
	}
	
	public void guess(Player player, String guess) throws NumberNotValidException{
		//because the action is the player to guess the answer of another player's, so first get the another model out.
		Duel1A2BPlayerBarModel anotherModel = playerModels.get(getAnotherPlayer(player));
		GuessResult result = anotherModel.guess(guess);
		detectWinner(player, result);
		playerModels.get(player).addRecord(new GuessRecord(guess, result));
	}
	
	private synchronized void detectWinner(Player player, GuessResult result){
		if (winner == null)
			winner = result.getA() == 4 ? player : winner;
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
	
	public boolean isGameOver(){
		return winner != null;
	}
	public Player getWinner() {
		return winner;
	}
	
	public Duel1A2BPlayerBarModel getPlayerBarModel(Player player){
		return playerModels.get(player);
	}
	
	public List<GuessRecord> getGuessRecords(Player player){
		return playerModels.get(player).getGuessRecords();
	}
}
