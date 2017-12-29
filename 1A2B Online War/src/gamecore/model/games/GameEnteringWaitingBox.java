package gamecore.model.games;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import gamecore.model.ClientPlayer;

/**
 * @author Waterball
 * 
 * After the game is launched, all the players will receive the launching game event, then each player
 * will be sent to the next scene where the game starts. But each player would cost a different time for going
 * to the game scene, this object is used for listening and checking when will the all game players 
 * have entered the game scene. The player client should send a protocol contains EnterGame event to
 * be removed from the unenteredPlayerIds set. 
 * 
 * Note that the game should only be started while all players entered,
 * otherwise there might be some players could have not registered the game callback.
 */
public class GameEnteringWaitingBox {
	private Set<String> unenteredPlayerIds = Collections.synchronizedSet(new HashSet<>());  
	private OnGamePlayersAllEnteredListener listener;
	
	public GameEnteringWaitingBox(OnGamePlayersAllEnteredListener listener, ClientPlayer ...clientPlayers){
		this.listener = listener;
		for (ClientPlayer clientPlayer : clientPlayers)
			unenteredPlayerIds.add(clientPlayer.getId());
	}
	
	public void enter(ClientPlayer clientPlayer){
		unenteredPlayerIds.remove(clientPlayer.getId());
		if (unenteredPlayerIds.isEmpty())
			listener.onAllPlayerEntered();
	}
	
	public interface OnGamePlayersAllEnteredListener{
		public void onAllPlayerEntered();
	}
}
