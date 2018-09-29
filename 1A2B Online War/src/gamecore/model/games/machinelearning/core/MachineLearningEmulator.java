package gamecore.model.games.machinelearning.core;

import container.protocol.ProtocolFactory;
import gamecore.model.GameMode;
import gamecore.model.games.Game;

public class MachineLearningEmulator extends Game{
	public MachineLearningEmulator(ProtocolFactory protocolFactory, GameMode gameMode, String roomId) {
		super(protocolFactory, gameMode, roomId);
	}
}
