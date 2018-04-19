package game.skill;

import java.util.List;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.Drain;
import game.actors.Fireball;
import game.actors.Player;

public class CastDrain implements Skill {

	private Player 		caster;
	private Game 		game;
	private float 		cooldown;
	private boolean		isActivated;
	private List<Player> players;
	
	public CastDrain(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
		isActivated = false;
		cooldown = 0.0f;
		this.players = game.getPlayers();
	}
	
	@Override
	public boolean use(Vec2 target) {
		if (!isActivated) {
			isActivated = true;
			cooldown = Constants.DRAIN_COOLDOWN;
			Drain d = new Drain(caster.getCenter(), target, Constants.FIREBALL_SIZE, game.getFreeId(), caster, players);
			game.addActor(d);
		}
		return isActivated;
	}

	@Override
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
	
	public int getId() {return Constants.DRAIN_ID;}
}
