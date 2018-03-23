package Game;

import java.awt.Point;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public abstract class Actor{
	public Vec2 position;
	public Rectangle hitbox;
	public Rectangle lowerBox;
	private Game game;
	private int id;
	
	public Actor(float x, float y, int w, int h, int lh, int id, Game g) {
		position = new Vec2(x,y);
		hitbox = new Rectangle((int)x,(int)y,w,h);
		lowerBox = new Rectangle((int)x, (int)y + (h - lh) , w, lh); // collision box dlya nog
		System.out.println("box: " + lowerBox.x);
		System.out.println("box: " + lowerBox.y);
		game = g;
		this.id = id;
	}
	
	public Actor(float x, float y, int w, int h) {
		position = new Vec2(x,y);
		hitbox = new Rectangle((int)x,(int)y,w,h);
		lowerBox = null;
	}

	public boolean collides() {
		GameArena arena = game.getArena();
		
		//collision with tile map part
		int left = lowerBox.x/arena.getTileSize();
		int right = (lowerBox.x + lowerBox.width)/arena.getTileSize();
		int up = lowerBox.y/arena.getTileSize();
		int bottom = (lowerBox.y + lowerBox.height)/arena.getTileSize();
		///System.out.println("box: " + lowerBox.x);
		//System.out.println("box: " + lowerBox.y);
		for(int i = up; i <= bottom; i++) {
			for(int j = left; j <= right; j++) {
				if(arena.getEntry(i, j) == 1)
					return true;
			}
		}
		System.out.println("Object collision");
		//collision with objects
		for(Actor b : game.getActors()) {
			if (this != b && hitbox.intersects(b.hitbox)) {
				System.out.println("Object" + this.id + "collided with " + b.id);
				return true;
			}
		}
		return false;
	}
	
	public void setPosition(int x, int y) {
		position.set(x, y);
		hitbox.setLocation(x, y);
		if(lowerBox != null)
			lowerBox.setLocation(x, y + (hitbox.height - lowerBox.height) );
	}
	
	public void addPosition(Vec2 p) {
		position.add(p);
		hitbox.setLocation((int)position.getX(), (int)position.getY());
		if(lowerBox != null)
			lowerBox.setLocation((int)position.getX(), (int)position.getY() + (hitbox.height - lowerBox.height) );
	}
	public Rectangle getHitbox() {return hitbox;}
	public Rectangle getLowerBox() {return lowerBox;}
	public Vec2 getPosition() {return position;}
	public int getId() {return id;}
	public abstract void update(long delta);
	public abstract Protocol.Client.Entity getState();
}
