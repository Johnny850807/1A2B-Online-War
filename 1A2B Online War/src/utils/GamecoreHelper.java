package utils;

import java.util.Collection;

import gamecore.entity.GameRoom;
import gamecore.entity.Player;
import gamecore.model.ClientPlayer;

public class GamecoreHelper {
	
	public static String clientsToString(Collection<ClientPlayer> clientPlayers){
		StringBuilder strb = new StringBuilder();
		for(ClientPlayer cp : clientPlayers)
			strb.append(cp.getPlayerName()).append(" ").append(cp.getPlayer().getUserStatus()).append("\n");
		return strb.toString();
	}
	
	public static String playersToString(Collection<Player> players){
		StringBuilder strb = new StringBuilder();
		for(Player cp : players)
			strb.append(cp.getName()).append(" ").append(cp.getUserStatus()).append("\n");
		return strb.toString();
	}
	
	public static String roomsToString(Collection<GameRoom> rooms){
		StringBuilder strb = new StringBuilder();
		for(GameRoom r : rooms)
			strb.append(r.getName()).append(" ");
		return strb.toString();
	}
}
