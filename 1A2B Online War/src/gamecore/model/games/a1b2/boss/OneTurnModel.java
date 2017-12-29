package gamecore.model.games.a1b2.boss;

import java.io.Serializable;
import java.util.List;

public class OneTurnModel implements Serializable{
	private List<PlayerSpirit> playerSpirits;
	private List<AttackResult> attackResults;
	private AttackResult lastedAttackResult;
	
	public OneTurnModel(List<PlayerSpirit> playerSpirits, List<AttackResult> attackResults,
			AttackResult lastedAttackResult) {
		this.playerSpirits = playerSpirits;
		this.attackResults = attackResults;
		this.lastedAttackResult = lastedAttackResult;
	}

	public List<PlayerSpirit> getPlayerSpirits() {
		return playerSpirits;
	}

	public List<AttackResult> getAttackResults() {
		return attackResults;
	}

	public AttackResult getLastedAttackResult() {
		return lastedAttackResult;
	}
	
	
}
