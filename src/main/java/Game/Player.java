package Game;

import java.nio.ByteBuffer;

public class Player extends Actor{
	private Input input;
	
	public Player(int x, int y, int w, int h, int lh, int ID, GameWorld gw) {
		super(x, y, w, h, lh, ID, gw);
		input = new Input();
	}
	
	public void update() {
		if(input.isKeyDown('w'))
			setPosition(getPosition().x, getPosition().y + 2);
		
		input.releaseAll();
	}
	
	public ByteBuffer getState() {
		int[] data = {position.x, position.y, 0};
		ByteBuffer state = ByteBuffer.allocate(data.length * 4);
		state.asIntBuffer().put(data);
		return state;
	}
}
