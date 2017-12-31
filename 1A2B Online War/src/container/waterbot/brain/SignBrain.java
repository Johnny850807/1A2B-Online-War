package container.waterbot.brain;

import container.protocol.Protocol;
import container.waterbot.Brain;
import container.waterbot.WaterBot;

public class SignBrain extends ChainBrain{

	public SignBrain(Brain next) {
		super(next);
	}

	@Override
	public void react(WaterBot waterBot, Protocol protocol) {
		
	}


}
