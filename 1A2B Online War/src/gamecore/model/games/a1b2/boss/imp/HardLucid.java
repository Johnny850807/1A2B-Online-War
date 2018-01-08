package gamecore.model.games.a1b2.boss.imp;

import container.core.MyLogger;
import container.protocol.ProtocolFactory;
import gamecore.model.games.a1b2.boss.core.IBoss1A2BGame;

public class HardLucid extends Lucid{

	public HardLucid(MyLogger log, ProtocolFactory protocolFactory) {
		super(log, protocolFactory);
	}

	@Override
	public void init(IBoss1A2BGame game) {
		super.init(game);
		setMaxHp(60000);
		setMp(10000);
	}
}
