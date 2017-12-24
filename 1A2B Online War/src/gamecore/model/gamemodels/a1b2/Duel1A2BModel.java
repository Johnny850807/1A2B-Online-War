package gamecore.model.gamemodels.a1b2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import container.Constants.Events.Games.Duel1A2B;
import gamecore.entity.Player;
import gamecore.model.GameMode;
import gamecore.model.gamemodels.GameModel;

/**
 * @author WaterBall
 * the model holding all the instances of one 1A2B-duel game.
 */
public class Duel1A2BModel extends GameModel{
	public static final int HOST = 0;
	public static final int PLAYER = 1;
	private Map<String, Duel1A2BPlayerBarModel> playerModels = new HashMap<>();  //<id, model>
	private Player winner = null;
	
	public Duel1A2BModel(Player host, Player anotherPlayer){
		super(GameMode.DUEL1A2B);
		playerModels.put(host.getId(), new Duel1A2BPlayerBarModel(host.getName()));
		playerModels.put(anotherPlayer.getId(), new Duel1A2BPlayerBarModel(anotherPlayer.getName()));
	}
	
	public void commitPlayerAnswer(Player player, String answer) throws NumberNotValidException{
		playerModels.get(player.getId()).setAnswer(answer);
	}
	
	public void guess(Player player, String guess) throws NumberNotValidException{
		//because the action is the player guessing the answer of another player's, so first get the another model out.
		Duel1A2BPlayerBarModel anotherModel = getAnotherPlayerModel(player);
		GuessResult result = anotherModel.guess(guess);
		detectWinner(player, result);
		playerModels.get(player.getId()).addRecord(new GuessRecord(guess, result));
	}
	
	private synchronized void detectWinner(Player player, GuessResult result){
		if (winner == null)
			winner = result.getA() == 4 ? player : winner;
	}
	
	public Duel1A2BPlayerBarModel getAnotherPlayerModel(Player player){
		for (String playerId : playerModels.keySet())
			if (!playerId.equals(player.getId()))
				return playerModels.get(playerId);
		throw new IllegalStateException();
	}
	
	public boolean isGameOver(){
		return winner != null;
	}
	public Player getWinner() {
		return winner;
	}
	
	public Duel1A2BPlayerBarModel getPlayerBarModel(Player player){
		return playerModels.get(player.getId());
	}
	
	public List<GuessRecord> getGuessRecords(Player player){
		return playerModels.get(player.getId()).getGuessRecords();
	}
}
