package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import container.protocol.ProtocolFactory;
import gamecore.model.GameMode;
import gamecore.model.games.Game;

public class Boss1A2BGame extends Game{
	private Boss boss;
	private List<PlayerBlock> playerBlocks = Collections.synchronizedList(new ArrayList<>());
	
	public Boss1A2BGame(ProtocolFactory protocolFactory, Boss boss, GameMode gameMode, String roomId) {
		super(protocolFactory, gameMode, roomId);
	}
	
	
}
