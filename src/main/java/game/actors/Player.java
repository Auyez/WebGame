package game.actors;

import game.Game;
import game.Input;
import game.Vec2;
import lobby.Protocol;

public class Player extends Actor{
	private Input input;
	private int speed;

	private static final byte ANIM_UP = 0;
	private static final byte ANIM_RIGHT = 1;
	private static final byte ANIM_DOWN = 2;
	private static final byte ANIM_LEFT = 3;
	private static final byte ANIM_IDLE = 4;

	public Player(float x, float y, int w, int h, int lh, int ID, Game gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
		speed = 200;
	}
	
	public void update(long delta) {
		Vec2 target = input.getMouse();
		setAnimation(ANIM_IDLE);

		if (target != null) {
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

			movement.scalar( (speed * (delta/1000.0f))/movement.getMagnitude() );
			movement.scalar(-1);
			addPosition(movement);

			if(collides() > -2) {
				movement.scalar(-1);
				addPosition(movement);
			}
			if (position.isClose(target, 2.0f)) {
				target = input.getNextTarget();
			}
		}
		input.releaseAll(); // looks like obsolete
	}

	public Input getInput() {
		return input;
	}
	public int getType() {return Actor.PLAYER;}
}
