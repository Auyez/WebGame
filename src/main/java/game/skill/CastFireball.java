package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Fireball;
import game.actors.Player;

public class CastFireball implements Skill{
	private Player 		caster;
	private Game 		game;
	private float 		cooldown;
	private boolean		isActivated;
	public CastFireball(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
		isActivated = false;
		cooldown = 0.0f;
	}
	
	@Override
	public boolean use(Vec2 target) {
		if (!isActivated) {
			isActivated = true;
			cooldown = Constants.FIREBALL_COOLDOWN;
			Fireball f = new Fireball(caster.getCenter(), target, Constants.FIREBALL_SIZE, game.getFreeId(), caster);
			game.addActor(f);
		}
		return isActivated;
	}
	
	public void update(long delta) {
		if (isActivated) {
			cooldown -= delta/1000.0f;
			if (cooldown < 0) {
				cooldown = 0.0f;
				isActivated = false;
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
