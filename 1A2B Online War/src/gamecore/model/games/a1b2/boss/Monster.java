package gamecore.model.games.a1b2.boss;

import container.base.MyLogger;
import container.protocol.ProtocolFactory;

public abstract class Monster extends AbstractSpirit{

	public Monster(String id, String name, MyLogger log, ProtocolFactory protocolFactory) {
		super(id, name, log, protocolFactory);
	}
	
	public abstract void init();
	
}
