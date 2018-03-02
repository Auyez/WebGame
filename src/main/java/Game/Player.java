package Game;

import java.nio.ByteBuffer;

public class Player extends Actor{
	private Input input;
	
	public Player(int x, int y, int w, int h, int lh, int ID, GameWorld gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
	}
	
	public void update() {
		int x = 0;
		int y = 0;
		int speed = 4;
		if(input.isKeyDown('w'))
			y -= speed;
		else if(input.isKeyDown('d'))
			x += speed;
		else if(input.isKeyDown('a'))
			x -= speed;
		else if(input.isKeyDown('s'))
			y += speed;
		setPosition(getPosition().x + x, getPosition().y + y);
		if(collides())
			setPosition(getPosition().x - x, getPosition().y - y);
		input.releaseAll();
	}
	
	public ByteBuffer getState() {
		//int[] data = {position.x, position.y, 0, getId()};
		ByteBuffer state = ByteBuffer.allocate(17);
		state.put((byte) 0); // always send 0 for now (player type = 0)
		state.putInt(position.x);
		state.putInt(position.y);
		state.putInt(0);
		state.putInt(getId());
		state.clear();
		return state;
	}
	
	public Input getInput() {
		return input;
	}
}
