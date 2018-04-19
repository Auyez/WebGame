package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Player;

public class Restore implements Skill{
	private Game 	game;
	private Player 	caster;
	private float	cooldown;
	private boolean isActivated;
	
	public Restore(Player caster, Game game) {
		this.game = game;
		this.caster = caster;
		cooldown = 0.0f;
		isActivated = false;
	}
	@Override
	//target - center of the position where we want to blink
	public boolean use(Vec2 target) {
		if(!isActivated) {
			cooldown = Constants.RESTORE_COOLDOWN;
			isActivated = true;
			if (caster.getHp() < Constants.PLAYER_HP) {
				caster.setHp(caster.getHp()+50);
			} 
		}
		return isActivated;
	}
	@Override
	public void update(long delta) {
		if (isActivated) {
			cooldown -= delta/1000.0f;
			if (cooldown < 0) {
				cooldown = Constants.RESTORE_COOLDOWN;
				isActivated = false;
				cooldown = 0;
			}
		}
	}
	@Override
	public float cooldown() {
		return cooldown;
	}
	
	@Override
	public void reset() {
		isActivated = false;
		cooldown = 0;
	}
}
