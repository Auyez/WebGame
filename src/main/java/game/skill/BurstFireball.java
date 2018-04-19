package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Fireball;
import game.actors.FireballBursted;
import game.actors.Player;

public class BurstFireball implements Skill{
	private Player 		caster;
	private Game 		game;
	private float 		cooldown;
	private boolean		isActivated;
	public BurstFireball(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
		isActivated = false;
		cooldown = 0.0f;
	}
	
	@Override
	public boolean use(Vec2 target) {
		if (!isActivated) {
			isActivated = true;
			cooldown = Constants.BURST_FIREBALL_COOLDOWN;
			FireballBursted f = new FireballBursted(caster.getCenter(), target, Constants.FIREBALL_SIZE, game.getFreeId(), caster);
			f.setDamage(Constants.BURST_FIREBALL_DMG);
			game.addActor(f);
			for(float i = 1, alpha = -8; i <= 4; i++, alpha += 16.0f/4.0f) {
				FireballBursted af = new FireballBursted(caster.getCenter(), calculateOffset(target, alpha), Constants.FIREBALL_SIZE, game.getFreeId() + (int)i, caster);
				f.setDamage(Constants.BURST_FIREBALL_DMG);
				game.addActor(af);
			}
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
	private Vec2 calculateOffset(Vec2 target, float degree) {
		Vec2 result = Vec2.subs(target, caster.getCenter());
		result.scalar(1.0f/result.getMagnitude());
		result.setX(result.getX() * (float)Math.cos(Math.toRadians(degree)) - result.getY() * (float)Math.sin(Math.toRadians(degree)));
		result.setY(result.getX() * (float)Math.sin(Math.toRadians(degree)) + result.getY() * (float)Math.cos(Math.toRadians(degree)));
		result.add(caster.getCenter());
		return result;
	}
	
	public int getId() {return Constants.BURST_FIREBALL_ID;}
}
