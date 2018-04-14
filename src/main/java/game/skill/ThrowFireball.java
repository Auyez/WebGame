package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Fireball;
import game.actors.Player;

public class ThrowFireball implements Skill{
	private Player 		caster;
	private Game 		game;
	private float 		calldown;
	private boolean		isActivated;
	public ThrowFireball(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
		isActivated = false;
		calldown = Constants.FIREBALL_CALLDOWN;
	}
	
	@Override
	public void use(Vec2 target) {
		if (!isActivated) {
			isActivated = true;
			Fireball f = new Fireball(caster.getCenter(), target, 30, game.getFreeId(), caster.getId());
			game.addActor(f);
		}
	}
	
	public void update(long delta) {
		if (isActivated) {
			calldown -= delta/1000.0f;
			if (calldown < 0) {
				calldown = Constants.FIREBALL_CALLDOWN;
				isActivated = false;
			}
		}
	}
}
