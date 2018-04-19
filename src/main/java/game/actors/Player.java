package game.actors;

import java.util.ArrayList;

import game.Constants;
import game.Input;
import game.Statistics;
import game.Vec2;
import game.skill.Skill;
import lobby.Protocol;
import lobby.Protocol.Client.PlayerStats;

public class Player extends Actor{
	private static final byte ANIM_UP = 0;
	private static final byte ANIM_RIGHT = 1;
	private static final byte ANIM_DOWN = 2;
	private static final byte ANIM_LEFT = 3;
	private static final byte ANIM_IDLE = 4;
	
	private int 			hp;
	private boolean			isDead;				//flag that indicates that Player is dead, required to control death and reviving
	private float			deathTimer;			//required to return player back in the game
	private Input 			input;				//contains information about input from front-end
	private int 			speed;				//speed of the player, we keep it in Constants (However it might be useful to keep it as variable)
	private Vec2 			updated_movement;	//need this field to keep track of our displacement (caused by overall bad design of this class)
	private Skill 			skills[];			//array of skills which are interfaces
	private Statistics		statistics;

	/*
	 * lh - means height of the lower hitbox of the player which is required when we collide with walls
	 * (So part of the player sprite can get over wall)
	 * ID - should be obtained from database and unique for each account in our system
	 * (It would be good to keep this number not big because game objects use it to obtain their own id (see getFreeId() method in game))
	 */
	public Player(float x, float y, int w, int h, int lh, int ID) {
		super(x, y, w, h, lh, ID);
		input = new Input();
		speed = Constants.PLAYER_SPEED;
		skills = new Skill[Constants.SKILL_NUMBER];
		hp = Constants.PLAYER_HP;
		deathTimer = Constants.PLAYER_DEATH_TIME;
		isDead = false;
		statistics = new Statistics();
	}
	
	public void update(long delta) {
		if (hp <= 0) {	//this block can return from this method
			if(!isDead) {	//here we set isDead flag
				isDead = true; // kill yourself
				input.clrMouse();
				for(Skill s : skills)
					s.reset();
				destroy();// remove yourself from actors (actors is a list in Game)
			}
			return; //don't do anything
		}
		
		Vec2 target = input.getMouse();
		setAnimation(ANIM_IDLE);

		for (int i = 0; i < Constants.SKILL_NUMBER; i++)//Constants.SKILL_NUMBER; i++)
			skills[i].update(delta);	//Update skills, for example their cooldown
		
		if (input.getActiveSkill() >= 0) {	//gets first pressed skill
			skills[input.getActiveSkill()].use(input.getSkillTarget());
		} else if (target != null) {
			Vec2 movement = Vec2.subs(target, getLowerCenter());

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
			
			movement.scalar( speed * (delta/1000.0f)/movement.getMagnitude() );
			addPosition(movement);
			updated_movement = movement;
			if (getLowerCenter().isClose(target, 2.0f))	// path-finding-getting next movement
				input.getNextTarget();
		}
	}

	//This is update method for the dead players
	//we need it because update() is called in the for loop over the actors
	//but dead players are excluded from that list(actors)
	//it is boolean in order to signal if player gets alive (bad design, but we have no time)
	public boolean update_dead(long delta) {
		if(isDead) {	//this method is only for dead players
			deathTimer -= delta/1000.0f;
			if (deathTimer <= 0) {	//block to revive the player
				hp = Constants.PLAYER_HP;
				deathTimer = Constants.PLAYER_DEATH_TIME;
				isDead = false;
				back();			//return player back to actors (the list in Game)
				return true;	//signal that player was revived
			}
		}
		return false; //signal that no reviving happened
	}
	
	
	//Reaction of the player to the collision
	public void resolve_collision(long delta, Actor a) {
		if ( a != null && (a.getType() == Actor.PLAYER || a.getType() == Actor.TILE) ) {
			input.clrMouse();	//stops any further movement (stops path-finding)
			setAnimation(ANIM_IDLE);
			updated_movement.scalar(-1.0f);
			addPosition(updated_movement);	//discard movement made in this update
		}
		input.releaseAll();	//releases any activated skill
	}
	
	public void setSkill(Skill s, byte skillIndex) {
		skills[skillIndex] = s;
	}
	
	public boolean isDead() { return isDead;}
	public void setHp(int hp) { this.hp = hp;}
	public int getHp() {return hp;}
	public Input getInput() {return input;}
	public int getType() {return Actor.PLAYER;}
	public Statistics getStatistics() {return statistics;}
	
	public ArrayList<lobby.Protocol.Client.Skill> getCooldowns() {
		ArrayList<Protocol.Client.Skill> skills_cooldowns = new ArrayList<Protocol.Client.Skill>();
		for (byte i = 0; i < Constants.SKILL_NUMBER; i++) {
			Protocol.Client.Skill cd = new Protocol.Client.Skill();
			cd.skillType = i;
			cd.cooldown = (int) Math.ceil(skills[i].cooldown());
			skills_cooldowns.add(cd);
		}
		return skills_cooldowns;
	}
	
	public Protocol.Client.Player getStats(){
		Protocol.Client.Player player = new Protocol.Client.Player();
		player.hp = hp;
		player.id = getId();
		return player;
	}

	public PlayerStats generateStats() {
		Protocol.Client.PlayerStats stats = new Protocol.Client.PlayerStats();
		stats.id = getId();
		stats.damage = getStatistics().getDamage();
		return stats;
	}
	
}
