package Game;

import java.nio.ByteBuffer;

public class Player extends Actor{
	private Input input;
	
	public Player(int x, int y, int w, int h, int lh, int ID, GameWorld gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
	}
	
	public void update() {
		int x,y;
		int speed = 2;
		x = getPosition().x;
		y = getPosition().y;
		if(input.isKeyDown('w'))
			y += speed;
		else if(input.isKeyDown('d'))
			x += speed;
		else if(input.isKeyDown('a'))
			x -= speed;
		else if(input.isKeyDown('s'))
			y -= speed;
		
		if(!collides())
			setPosition(x,y);
		input.releaseAll();
	}
	
	public ByteBuffer getState() {
		int[] data = {0, position.x, position.y, 0, getId()};
		ByteBuffer state = ByteBuffer.allocate(data.length * 4);
		state.asIntBuffer().put(data);
		return state;
	}
	
	
}
