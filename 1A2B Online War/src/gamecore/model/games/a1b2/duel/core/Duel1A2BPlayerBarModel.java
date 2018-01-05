package gamecore.model.games.a1b2.duel.core;

import java.util.ArrayList;
import java.util.List;

import gamecore.model.games.a1b2.A1B2NumberValidator;


public class Duel1A2BPlayerBarModel {
	private String playerId;
	private String name;
	private String answer;
	private List<GuessRecord> guessRecords = new ArrayList<>();
	
	public Duel1A2BPlayerBarModel(String playerId, String playerName) {
		this.playerId = playerId;
		this.name = playerName;
	}

	public String getPlayerId() {
		return playerId;
	}
	
	public String getName() {
		return name;
	}

	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) throws NumberNotValidException {
		A1B2NumberValidator.validateNumber(answer);
		this.answer = answer;
	}
	
	public GuessResult guess(String guess) throws NumberNotValidException{
		A1B2NumberValidator.validateNumber(guess);
		return A1B2NumberValidator.getGuessResult(answer, guess);
	}
	
	public void addRecord(GuessRecord guessRecord) throws NumberNotValidException{
		A1B2NumberValidator.validateNumber(guessRecord.getGuess());
		this.guessRecords.add(guessRecord);
	}
	
	public GuessRecord getRecord(int position){
		return guessRecords.get(position);
	}
	
	public List<GuessRecord> getGuessRecords() {
		return guessRecords;
	}
	
	public int getGuessingTimes(){
		return guessRecords.size();
	}
}
