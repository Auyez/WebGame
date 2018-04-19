package game.actors;

import game.Constants;
import game.Vec2;

public class LightningBolt extends Actor {
	private int 		speed;
	private Vec2 		direction;
	private Player 		parent;
	private int 		traveled;
	
	public LightningBolt(Vec2 start, Vec2 target, int size, int id, Player parent) {
		super(0, 0, size, size, id);
		setCenter(start);
		this.direction = Vec2.subs(target, getCenter());
		this.direction.scalar(1.0f/this.direction.getMagnitude()); //calculate direction vector
		setSpriteAngle((int)Math.toDegrees(this.direction.getAngleRad()));
		this.parent = parent;
		traveled = 0;
		speed = Constants.LIGHTNINGBOLT_SPEED;
	}

	@Override
	public void update(long delta) {
		Vec2 movement = new Vec2(direction);
		movement.scalar(speed * (delta/1000.0f));
		addPosition(movement);
		traveled += movement.getMagnitude();
		if (traveled > Constants.LIGHTNINGBOLT_RANGE)
			destroy();
	}

	@Override
	public void resolve_collision(long delta, Actor a) {
		if (a != null && !isDestroyed()) {
			if (a.getId() != parent.getId()) {
				if(a.isProjectile())
					a.destroy();
				if(a.getType() == Actor.PLAYER) {
					Player p = (Player) a;
					p.setHp(p.getHp() - Constants.LIGHTNINGBOLT_DMG);
					parent.getStatistics().damageDone(Constants.LIGHTNINGBOLT_DMG);
				}
				destroy();
			}
		}
	}

	@Override
	public int getType() {
		return Actor.LIGHTNINGBOLT;
	}
}
