package container.core;

import gamecore.model.ClientPlayer;

public interface ClientBinder {
	ClientPlayer getClientPlayer(String playerId);
}
