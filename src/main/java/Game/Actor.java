package Game;

import java.awt.Point;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

public abstract class Actor{
	public Point position;
	public Rectangle hitbox;
	public Rectangle lowerBox;
	private GameWorld gw;
	private int id;
	
	public Actor(int x, int y, int w, int h, int lh, int id, GameWorld gw) {
		position = new Point(x,y);
		hitbox = new Rectangle(x,y,w,h);
		lowerBox = new Rectangle(x,y,w,lh);
		this.gw = gw;
		this.id = id;
	}
	
	public Actor(int x, int y, int w, int h) {
		position = new Point(x,y);
		hitbox = new Rectangle(x,y,w,h);
		lowerBox = null;
	}
	
	public abstract void update();
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public Rectangle getLowerBox() {
		return lowerBox;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(int x, int y) {
		position.setLocation(x, y);
	}
	
	public abstract ByteBuffer getState();
}
