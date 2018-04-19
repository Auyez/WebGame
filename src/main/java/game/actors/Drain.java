package game.actors;

import java.util.List;

import game.Constants;
import game.Vec2;

public class Drain extends Actor implements Projectile{
	private int 		speed;
	private Vec2 		direction;
	private Player 		parent;
	private int 		traveled;
	private List<Player> players;
	private boolean 	redirected;
	
	public Drain(Vec2 start, Vec2 target, int size, int id, Player parent, List<Player> players) {
		super(0, 0, size, size, id);
		setCenter(start);
		this.direction = Vec2.subs(target, getCenter());
		this.direction.scalar(1.0f/this.direction.getMagnitude()); //calculate direction vector
		setSpriteAngle((int)Math.toDegrees(this.direction.getAngleRad()));
		this.parent = parent;
		traveled = 0;
		speed = Constants.DRAIN_SPEED;
		this.players = players;
		redirected = false;
	}

	@Override
	public void update(long delta) {
		Vec2 movement = new Vec2(direction);
		movement.scalar(speed * (delta/1000.0f));
		addPosition(movement);
		traveled += movement.getMagnitude();
		if (traveled > Constants.DRAIN_RANGE / 2 && !redirected) {
			direction = findTarget();
		}
		
		if (traveled > Constants.DRAIN_RANGE)
			destroy();
	}
	// TODO : find closest player
	private Vec2 findTarget() {
		Vec2 position = getCenter();
		float closest = Constants.GAME_WIDTH + Constants.GAME_WIDTH; //max possible distance
		Player target = null;
		for (Player p : players) {
			float distance = Vec2.subs(p.getLowerCenter(), position).getMagnitude();
			if (distance < closest) {
				target = p;
				closest = distance;
			}
		}
		redirected = true;
		Vec2 direction = Vec2.subs(target.getLowerCenter(), position);
		direction.scalar(1.0f/direction.getMagnitude());
		return direction;
	}

	@Override
	public void resolve_collision(long delta, Actor a) {
		if (a != null && !isDestroyed()) {
			if (a.getId() != parent.getId()) {
				if(a.isProjectile() && ((Projectile) a).getParentId() != parent.getId()) {
					a.destroy();
					destroy();
				}else if(a.getType() == Actor.PLAYER) {
					Player p = (Player) a;
					p.setHp(p.getHp() - Constants.DRAIN_DMG);
					parent.getStatistics().damageDone(Constants.DRAIN_DMG);
					parent.getStatistics().skillDamage(Constants.DRAIN_ID, Constants.DRAIN_DMG);
					if (parent.getHp() + Constants.DRAIN_DMG > Constants.PLAYER_HP) {
						parent.setHp(Constants.PLAYER_HP);
					} else {
						parent.setHp(parent.getHp() + Constants.DRAIN_DMG);
					}
					destroy();
				}else if (a.getType() == Actor.TILE){
					destroy();
				}
			}
		}
	}

	@Override
	public int getType() {
		return Actor.DRAIN;
	}

	@Override
	public int getParentId() {
		return parent.getId();
	}
}
