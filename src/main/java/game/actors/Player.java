package game.actors;

import game.Game;
import game.Input;
import game.Vec2;
import lobby.Protocol;

public class Player extends Actor{
	private Input input;
	private int speed;
	public Player(float x, float y, int w, int h, int lh, int ID, Game gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
		speed = 200;
	}
	
	public void update(long delta) {
		Vec2 target = input.getMouse();
		if (target != null) {
			Vec2 movement = Vec2.subs(position, target);
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
	
	public Protocol.Client.Entity getState() {
		Protocol.Client.Entity state = new Protocol.Client.Entity();
		state.player = new Protocol.Client.Player();
		state.player.x = (int) position.getX();
		state.player.y = (int) position.getY();
		state.player.a = 0;
		state.player.id = getId();

		return state;
	}	
	public Input getInput() {
		return input;
	}
	public Types getType() {return Types.PLAYER;}
}
