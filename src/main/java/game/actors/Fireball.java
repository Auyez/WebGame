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
		target.add(new Vec2(-getHitbox().width/2.0f, -getHitbox().height/2.0f)); //adjust target such that center of fireball would be on center
		this.direction = Vec2.subs(target, position); 
		this.direction.scalar(1.0f/this.direction.getMagnitude()); //calculate direction vector
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
		if (a != null && !isDestroyed()) {
			if (a.getId() != parentId) {
				if(a.getType() == Actor.FIREBALL)
					a.destroy();
				if(a.getType() == Actor.PLAYER) {
					Player p = (Player) a;
					p.setHp(p.getHp() - Constants.FIREBALL_DMG);
				}
				destroy();
			}
		}
	}

	@Override
	public int getType() {
		return Actor.FIREBALL;
	}

}
