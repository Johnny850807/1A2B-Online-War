package gamecore.model.games.a1b2.boss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackActionModel implements Iterable<AttackResult>{
	private AbstractSpirit attacker;
	private int mpCost;
	private List<AttackResult> attackResults = new ArrayList<>();
	
	public AttackActionModel(int mpCost, AbstractSpirit attacker) {
		this.attacker = attacker;
		this.mpCost = mpCost;
	}
	
	public void addAttackResult(AttackResult attackResult){
		attackResults.add(attackResult);
	}
	
	public void setAttackResults(List<AttackResult> attackResults) {
		this.attackResults = attackResults;
	}

	public int getMpCost() {
		return mpCost;
	}
	
	public AbstractSpirit getAttacker() {
		return attacker;
	}

	public void setAttacker(PlayerSpirit attacker) {
		this.attacker = attacker;
	}

	public List<AttackResult> getAttackResults() {
		return attackResults;
	}

	@Override
	public Iterator<AttackResult> iterator() {
		return attackResults.iterator();
	}
	
	
}
