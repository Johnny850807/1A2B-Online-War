package gamecore.model.games.a1b2.duel.core;

public interface GuessStrategy {
	void feedRecord(GuessRecord guessRecord);
	String nextGuess();
}
