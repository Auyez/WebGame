package Game;

import java.nio.ByteBuffer;

public class Player extends Actor{
	private Input input;
	private int speed;
	public Player(float x, float y, int w, int h, int lh, int ID, Game gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
		speed = 80;
	}
	
	public void update(long delta) {
		/**System.out.println(position);
		Vec2 movement = new Vec2(0, 0);
		float val = speed*(delta/1000.0f);
		if(input.isKeyDown('w'))
			movement.setY( -1 );
		else if(input.isKeyDown('s'))
			movement.setY(1);
		if(input.isKeyDown('d'))
			movement.setX( 1 );
		else if(input.isKeyDown('a'))
			movement.setX( -1 );**/
		//movement.scalar(speed * (delta/1000.0f));
		if (input.getMouse() != null) {
			Vec2 movement = Vec2.subs(position, input.getMouse());
			movement.scalar(-1);
			addPosition(movement);
			if(collides() > -2) {
				movement.scalar(-1);
				addPosition(movement);
			}
			if (position.equals(input.getMouse()))
				input.clrMouse();
		}
		input.releaseAll();
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
