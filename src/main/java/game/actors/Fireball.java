package game.actors;

import game.Constants;
import game.Vec2;

public class Fireball extends Actor implements Projectile{
	private int 		speed;
	private Vec2 		direction;
	private Player 		parent;
	private int 		traveled;
	private int			damage;
	
	public Fireball(Vec2 start, Vec2 target, int size, int id, Player parent) {
		super(0, 0, size, size, id);
		setCenter(start);
		this.direction = Vec2.subs(target, getCenter());
		this.direction.scalar(1.0f/this.direction.getMagnitude()); //calculate direction vector
		setSpriteAngle((int)Math.toDegrees(this.direction.getAngleRad()));
		this.parent = parent;
		traveled = 0;
		speed = Constants.FIREBALL_SPEED;
		damage = Constants.FIREBALL_DMG;
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
			if (a.getId() != parent.getId() ) {
				if(a.isProjectile() && ((Projectile) a).getParentId() != parent.getId()) {
					a.destroy();
					destroy();
				}else if(a.getType() == Actor.PLAYER) {
					Player p = (Player) a;
					p.setHp(p.getHp() - damage);
					parent.getStatistics().damageDone(damage);
					parent.getStatistics().skillDamage(Constants.FIREBALL_ID, Constants.FIREBALL_DMG);
					destroy();
				}else if (a.getType() == Actor.TILE){
					destroy();
				}
			}
		}
	}

	@Override
	public int getType() {
		return Actor.FIREBALL;
	}

	@Override
	public int getParentId() {
		return parent.getId();
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
}
