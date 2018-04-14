package game.skill;

import game.Game;
import game.Vec2;
import game.actors.Fireball;
import game.actors.Player;

public class ThrowFireball implements Skill{
	private Player 		caster;
	private  Game 		game;
	public ThrowFireball(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
	}
	
	@Override
	public void use(Vec2 target) {
		Fireball f = new Fireball(caster.getPosition(), target, 30, game.getFreeId(), caster.getId());
		game.addActor(f);
	}
}
