package gamecore.model.games.a1b2.boss;

import java.util.List;

import gamecore.model.ClientPlayer;

public interface BossAction {
	void execute(Boss1A2BGame game, List<ClientPlayer> players);
}
