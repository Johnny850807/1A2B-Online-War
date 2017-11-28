package gamecore.model.gamemodels.a1b2;

public class GuessRecord {
	private String guess;
	private GuessResult result;
	
	public GuessRecord(String guess, GuessResult result) {
		setGuess(guess);
		setResult(result);
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}

	public GuessResult getResult() {
		return result;
	}

	public void setResult(GuessResult result) {
		this.result = result;
	}
	
	
}
