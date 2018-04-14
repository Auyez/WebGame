package game.actors;

import game.Constants;
import game.Vec2;

public class Fireball extends Actor{
	private int 		speed;
	private Vec2 		direction;
	private int 		parentId;
	private int 		traveled;
	
	public Fireball(Vec2 start, Vec2 target, int size, int id, int parentId) {
		super(start.getX(), start.getY(), size, size, id);
		this.direction = Vec2.subs(target, position);
		this.direction.scalar(1.0f/this.direction.getMagnitude());
		this.parentId = parentId;
		traveled = 0;
		speed = Constants.FIREBALL_SPEED;
	}

	@Override
	public void update(long delta) {
		Vec2 movement = new Vec2(direction);
		movement.scalar(speed * (delta/1000.0f));
		addPosition(movement);
		traveled += movement.getMagnitude();
		if (traveled > Constants.FIREBALL_RANGE)
			destroy();
	}

	@Override
	public void resolve_collision(long delta, Actor a) {
		if (a != null && !a.isDestroyed()) {
			if (a.getId() != parentId)
				destroy();
		}
	}

	@Override
	public int getType() {
		return Actor.FIREBALL;
	}

}
