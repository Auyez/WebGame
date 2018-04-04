package game.actors;

import java.awt.Rectangle;

import game.Game;
import game.GameArena;
import game.Vec2;
import lobby.Protocol;

public abstract class Actor{
	public static enum Types{
		PLAYER;					// add types here
	};
	
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
	

	//returns -2 if no collision happens, or -1 if actor collides with arena, otherwise returns id
	public int collides() {
		GameArena arena = game.getArena();
		
		//collision with tile map part
		int left = lowerBox.x/arena.getTileSize();
		int right = (lowerBox.x + lowerBox.width)/arena.getTileSize();
		int up = lowerBox.y/arena.getTileSize();
		int bottom = (lowerBox.y + lowerBox.height)/arena.getTileSize();

		for(int i = up; i <= bottom; i++) {
			for(int j = left; j <= right; j++) {
				if(arena.getEntry(i, j) == 1)
					return -1;
			}
		}
		//collision with objects
		for(Actor b : game.getActors()) {
			if (this != b && hitbox.intersects(b.hitbox)) {
				return b.id;
			}
		}
		return -2;
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
	public abstract Types getType();
}
