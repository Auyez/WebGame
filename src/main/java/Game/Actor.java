package Game;

import java.awt.Point;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public abstract class Actor{
	public Point position;
	public Rectangle hitbox;
	public Rectangle lowerBox;
	private GameWorld gw;
	private int id;
	
	public Actor(int x, int y, int w, int h, int lh, int id, GameWorld gw) {
		position = new Point(x,y);
		hitbox = new Rectangle(x,y,w,h);
		lowerBox = new Rectangle(x, y + (h - lh) , w, lh); // collision box dlya nog
		this.gw = gw;
		this.id = id;
	}
	
	public Actor(int x, int y, int w, int h) {
		position = new Point(x,y);
		hitbox = new Rectangle(x,y,w,h);
		lowerBox = null;
	}

	public boolean collides() {
		GameArena arena = gw.getArena();
		
		//collision with tile map part
		int left = lowerBox.x/arena.getTileSize();
		int right = (lowerBox.x + lowerBox.width)/arena.getTileSize();
		int up = lowerBox.y/arena.getTileSize();
		int bottom = (lowerBox.y + lowerBox.height)/arena.getTileSize();
		
		for(int i = up; i <= bottom; i++) {
			for(int j = left; j <= right; j++) {
				if(arena.getEntry(i, j) == 1)
					return true;
			}
		}
		
		//collision with objects
		for(Actor b : gw.getActors()) {
			if (this != b && hitbox.intersects(b.hitbox))
				return true;
		}
		return false;
	}
	
	public void setPosition(int x, int y) {
		position.setLocation(x, y);
		hitbox.setLocation(x, y);
		if(lowerBox != null) {
			lowerBox.setLocation(x, y + (hitbox.height - lowerBox.height) );
		}
	}
	
	public Rectangle getHitbox() {return hitbox;}
	public Rectangle getLowerBox() {return lowerBox;}
	public Point getPosition() {return position;}
	public int getId() {return id;}
	public abstract void update();
	public abstract Protocol.Client.Entity getState();
}
