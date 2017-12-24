package gamecore.model.gamemodels.a1b2;

public class SetAnswerModel {
	private String playerId;
	private String answer;
	
	public SetAnswerModel(String playerId, String answer) {
		this.playerId = playerId;
		this.answer = answer;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
