package container.waterbot.brain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import container.protocol.Protocol;
import container.waterbot.Brain;
import container.waterbot.WaterBot;

public abstract class ChainBrain implements Brain{
	private static Logger log = LogManager.getLogger(ChainBrain.class);
	private Brain next;
	
	public ChainBrain(Brain next) {
		this.next = next;
	}

	protected void nextIfNotNull(WaterBot waterBot, Protocol protocol){
		if (next != null)
		{
			log.trace(getLogPrefix(waterBot) + "end thinking, propogate to the next chain node: " + next.getClass().getSimpleName());
			next.react(waterBot, protocol);
		}
		else
			log.trace(getLogPrefix(waterBot) + "end thinking, the chain reachs the end.");
	}
	
	protected String getLogPrefix(WaterBot bot){
		return "Bot " + bot.getName() + " - " + getClass().getSimpleName() + " ";
	}
}
