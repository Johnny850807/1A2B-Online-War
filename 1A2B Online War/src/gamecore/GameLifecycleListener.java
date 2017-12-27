package gamecore;

import gamecore.model.ClientPlayer;
import gamecore.model.games.Game;
import gamecore.model.games.a1b2.GameOverModel;

public interface GameLifecycleListener {
	public void onGameStarted(Game game);
	public void onGameInterrupted(Game game, ClientPlayer noResponsePlayer);
	public void onGameOver(Game game, GameOverModel gameOverModel);
}
