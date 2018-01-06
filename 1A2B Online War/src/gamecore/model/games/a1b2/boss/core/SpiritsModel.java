package gamecore.model.games.a1b2.boss.core;

import java.util.List;

import gamecore.model.games.a1b2.boss.core.AttackResult.AttackType;

public class SpiritsModel {
	private Monster boss;
	private List<PlayerSpirit> playerSpirits;
	private transient OnAttackActionRender onAttackActionRender;
	
	public SpiritsModel(Monster boss, List<PlayerSpirit> playerSpirits) {
		this.boss = boss;
		this.playerSpirits = playerSpirits;
	}
	
	public void setOnAttackActionParsingListener(OnAttackActionRender onAttackActionRender) {
		this.onAttackActionRender = onAttackActionRender;
	}
	
	public Monster getBoss() {
		return boss;
	}

	public List<PlayerSpirit> getPlayerSpirits() {
		return playerSpirits;
	}
	
	public PlayerSpirit getPlayerSpirit(int index){
		return playerSpirits.get(index);
	}
	
	public AbstractSpirit getSpiritByPlayerId(String spiritId){
		if (boss.getId().equals(spiritId))
			return boss;
		for (PlayerSpirit playerSpirit : playerSpirits)
			if (playerSpirit.getId().equals(spiritId))
				return playerSpirit;
		throw new IllegalArgumentException("The spirit with the id " + spiritId + " not found in the spirits model.");
	}
	
	/**
	 * this method will update all spirits' hp and mp by parsing all the attack result from the passed model.
	 * @param actionModel the attack action model
	 */
	public void updateHPMPFromTheAttackActionModel(AttackActionModel actionModel){
		AbstractSpirit whosTurn = getSpiritByPlayerId(actionModel.getAttacker().getId());
		if (onAttackActionRender != null && actionModel.getMpCost() != 0)
			onAttackActionRender.onDrawMpCosted(whosTurn, actionModel.getMpCost());
		whosTurn.costMp(actionModel.getMpCost());
		
		for (AttackResult attackResult : actionModel)
		{
			AbstractSpirit attacker = getSpiritByPlayerId(attackResult.getAttacker().getId());
			AbstractSpirit attacked = getSpiritByPlayerId(attackResult.getAttacked().getId());
			if (onAttackActionRender != null && attackResult.getDamage() != 0)
				onAttackActionRender.onDrawHpCosted(attacked, attackResult.getDamage());
			attacked.costHp(attackResult.getDamage());
			
			if (onAttackActionRender != null)
			{
				AttackType attackType = attackResult.getAttackType();
				if (attackType == AttackType.MAGIC)
					onAttackActionRender.onDrawMagicAttack(attacked, attacker, attackResult);
				else if (attackType == AttackType.NORMAL)
					onAttackActionRender.onDrawNormalAttack(attacked, attacker, attackResult);
			}
		}
	}
	
	/**
	 * the asynchronous version of the updateHPMPFromTheAttackActionModel() method.
	 */
	public void updateHPMPFromTheAttackActionModelAsync(AttackActionModel actionModel){
		new Thread(){
			@Override
			public void run() {
				updateHPMPFromTheAttackActionModel(actionModel);
			}
		}.start();
	}
	
	public interface OnAttackActionRender{
		void onDrawHpCosted(AbstractSpirit spirit, int costMp);
		void onDrawMpCosted(AbstractSpirit spirit, int costHp);
		void onDrawNormalAttack(AbstractSpirit attacked, AbstractSpirit attacker, AttackResult attackResult);
		void onDrawMagicAttack(AbstractSpirit attacked, AbstractSpirit attacker, AttackResult attackResult);
	}
	
	
}
