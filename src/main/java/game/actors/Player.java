package game.actors;

import game.Constants;
import game.Game;
import game.Input;
import game.Vec2;
import game.skill.Skill;
import lobby.Protocol;

public class Player extends Actor{
	private static final byte ANIM_UP = 0;
	private static final byte ANIM_RIGHT = 1;
	private static final byte ANIM_DOWN = 2;
	private static final byte ANIM_LEFT = 3;
	private static final byte ANIM_IDLE = 4;
	
	private int 		hp;
	private boolean		isDead;
	private float		deathTimer;
	private Input 		input;
	private int 		speed;
	private Vec2 		updated_movement;
	private Skill 		skills[];


	public Player(float x, float y, int w, int h, int lh, int ID) {
		super(x, y, w, h, lh, ID);
		input = new Input();
		speed = 200;
		skills = new Skill[Constants.SKILL_NUMBER];
		hp = Constants.PLAYER_HP;
		deathTimer = Constants.PLAYER_DEATH_TIME;
		isDead = false;
	}
	
	public void update(long delta) {
		if (hp <= 0) {
			if(!isDead) {
				isDead = true; // kill yourself
				destroy();// remove yourself from actors
			}
			return; //don't do anything
		}
		
		Vec2 target = input.getMouse();
		setAnimation(ANIM_IDLE);

		for (int i = 0; i < 2; i++)//Constants.SKILL_NUMBER; i++)
			skills[i].update(delta);
		
		if (input.getActiveSkill() >= 0) {
			skills[input.getActiveSkill()].use(input.getSkillTarget());
		} else if (target != null) {
			Vec2 movement = Vec2.subs(position, target);

			// animation
			int angle = (int)Math.round(Math.toDegrees(movement.getAngleRad()));
			if (angle < 45)
				setAnimation(ANIM_LEFT);
			else if (angle < 45*3)
				setAnimation(ANIM_UP);
			else if (angle < 45*5)
				setAnimation(ANIM_RIGHT);
			else if (angle < 45*7)
				setAnimation(ANIM_DOWN);
			else
				setAnimation(ANIM_LEFT);
			// animation

			movement.scalar( -(speed * (delta/1000.0f))/movement.getMagnitude() );
			addPosition(movement);
			updated_movement = new Vec2(movement.getX(), movement.getY());

			if (position.isClose(target, 2.0f))
				input.getNextTarget();
		}
	}

	public boolean update_dead(long delta) {
		if(isDead) {
			deathTimer -= delta/1000.0f;
			if (deathTimer <= 0) {
				hp = Constants.PLAYER_HP;
				deathTimer = Constants.PLAYER_DEATH_TIME;
				Vec2 spawn = new Vec2(Constants.SPAWN_POINTS[0]);	// get new spawn point
				spawn.scalar(Constants.GAME_TILE_SIZE);				// convert to pixels
				setPosition(spawn);									// move
				isDead = false;
				back();												//return player back to actors
				return true;	//signal that player was revived
			}
		}
		return false;
	}
	
	public void resolve_collision(long delta, Actor a) {
		if ( a != null && (a.getType() == Actor.PLAYER || a.getType() == Actor.TILE) ) {
			input.clrMouse();
			setAnimation(ANIM_IDLE);
			updated_movement.scalar(-1.0f);
			addPosition(updated_movement);
		}
		input.releaseAll();
	}
	
	public void setSkill(Skill s, byte skillIndex) {
		skills[skillIndex] = s;
	}
	
	public boolean isDead() { return isDead;}
	public void setHp(int hp) { this.hp = hp;}
	public int getHp() {return hp;}
	public Input getInput() {return input;}
	public int getType() {return Actor.PLAYER;}
}
