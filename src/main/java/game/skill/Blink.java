package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Player;

public class Blink implements Skill{
	private Game 	game;
	private Player 	caster;
	private float	cooldown;
	private boolean isActivated;
	public Blink(Player caster, Game game) {
		this.game = game;
		this.caster = caster;
		cooldown = Constants.BLINK_COOLDOWN;
		isActivated = false;
	}
	@Override
	public void use(Vec2 target) {
		if(!isActivated) {
			isActivated = true;
			target.add(new Vec2(-caster.getHitbox().width/2.0f, -caster.getHitbox().height/2.0f));
			Vec2 move = Vec2.subs(target, caster.getPosition());
			if (move.getMagnitude() > Constants.BLINK_RANGE)
				move.scalar(Constants.BLINK_RANGE/move.getMagnitude());
			caster.addPosition(move);
			if (game.collides(caster) != null) {
				move.scalar(-1.0f);
				caster.addPosition(move);
				isActivated = false;
			}
		}
	}
	@Override
	public void update(long delta) {
		if (isActivated) {
			cooldown -= delta/1000.0f;
			if (cooldown < 0) {
				cooldown = Constants.BLINK_COOLDOWN;
				isActivated = false;
			}
		}
	}
	@Override
	public float cooldown() {
		return cooldown;
	}	
}
