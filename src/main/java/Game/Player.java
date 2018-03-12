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
	
	public Protocol.Client.Entity getState() {
		Protocol.Client.Entity state = new Protocol.Client.Entity();
		state.player = new Protocol.Client.Player();
		state.player.x = position.x;
		state.player.y = position.y;
		state.player.a = 0;
		state.player.id = getId();

		return state;
	}
	
	public Input getInput() {
		return input;
	}
}
