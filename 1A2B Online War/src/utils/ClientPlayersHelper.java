package utils;

import java.util.Collection;

import gamecore.model.ClientPlayer;

public class ClientPlayersHelper {
	
	public static String toString(Collection<ClientPlayer> clientPlayers){
		StringBuilder strb = new StringBuilder();
		for(ClientPlayer cp : clientPlayers)
			strb.append(cp.getPlayerName()).append(" ");
		return strb.toString();
	}
}
