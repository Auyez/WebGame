package game.skill;

import game.Game;
import game.Vec2;
import game.actors.Fireball;
import game.actors.Player;

public class ThrowFireball extends Skill{
	public ThrowFireball(Player p, Game game) {
		super(p, game);
	}
	
	@Override
	public void use(Vec2 target) {
		Fireball f = new Fireball(caster.position, target, 30, game.getFreeId(), caster.getId());
		game.addActor(f);
	}
}
