package gamecore.model.games.a1b2.boss;

import java.util.List;
import java.util.Random;

import gamecore.model.ClientPlayer;

public class BasicBoss extends Boss{
	
	@Override
	protected void init() {
		
	}
	
	@Override
	protected void damage(ClientPlayer player, String guess) {

	}

	@Override
	protected void action(List<PlayerBlock> playerBlocks) {
		int randomTarget = new Random().nextInt(playerBlocks.size());
		PlayerBlock target = playerBlocks.get(randomTarget);
		
	}
}
