package game.skill;
import game.Vec2;

public interface Skill {
	public void use(Vec2 target);
	public void update(long delta);
}
