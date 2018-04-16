package game.skill;
import game.Vec2;

public interface Skill {
	public boolean use(Vec2 target);
	public void update(long delta);
	public float cooldown();
}
