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
		cooldown = 0.0f;
		isActivated = false;
	}
	@Override
	//target - center of the position where we want to blink
	public boolean use(Vec2 target) {
		if(!isActivated) {
			cooldown = Constants.BLINK_COOLDOWN;
			isActivated = true;
			target.add(new Vec2(-caster.getHitbox().width/2.0f, -caster.getHitbox().height/2.0f)); //calculate true position of target
			Vec2 move = Vec2.subs(target, caster.getPosition()); //vector from player to target
			if (move.getMagnitude() > Constants.BLINK_RANGE)
				move.scalar(Constants.BLINK_RANGE/move.getMagnitude());
			caster.addPosition(move);
			if (game.collides(caster) != null) { //checking 
				move.scalar(-1.0f);
				caster.addPosition(move);
				isActivated = false;
				cooldown = 0.0f;
			} else {
				caster.getInput().clrMouse();
				caster.getStatistics().skillUsed(getId());
			}
		}
		return isActivated;
	}
	@Override
	public void update(long delta) {
		if (isActivated) {
			cooldown -= delta/1000.0f;
			if (cooldown < 0) {
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
	
	public int getId() {return Constants.BLINK_ID;}
}
