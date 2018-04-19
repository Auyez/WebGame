package game.skill;

import game.Constants;
import game.Game;
import game.Vec2;
import game.actors.LightningBolt;
import game.actors.Player;

public class CastLightningBolt implements Skill {
	private Player 		caster;
	private Game 		game;
	private float 		cooldown;
	private boolean		isActivated;
	
	public CastLightningBolt(Player caster, Game game) {
		this.caster = caster;
		this.game = game;
		isActivated = false;
		cooldown = 0.0f;
	}
	
	@Override
	public boolean use(Vec2 target) {
		if (!isActivated) {
			caster.getStatistics().skillUsed(getId());
			isActivated = true;
			cooldown = Constants.LIGHTNINGBOLT_COOLDOWN;
			LightningBolt l = new LightningBolt(caster.getCenter(), target, Constants.LIGHTNINGBOLT_SIZE, game.getFreeId(), caster);
			game.addActor(l);
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
	
	public int getId() {return Constants.LIGHTNINGBOLT_ID;}
}
