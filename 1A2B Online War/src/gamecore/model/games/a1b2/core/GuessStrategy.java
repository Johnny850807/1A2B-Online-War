package gamecore.model.games.a1b2.core;

/**
 * @author Waterball
 * The algorithm interface of the guessing game.
 */
public interface GuessStrategy {
	/**
	 * @return the next guess
	 */
	String nextGuess();
	
	/**
	 * @param guessRecord the new record from the last guess
	 */
	void feedRecord(GuessRecord guessRecord);
}
