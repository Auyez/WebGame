package game.actors;

public class Projectile extends Actor {
	
	private int speed;
	
	public Projectile(float x, float y, int x_dir, int y_dir, int size, int id, int speed) {
		super(x, y, size, id);
		this.speed = speed;
	}

	@Override
	public void update(long delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve_collision(long delta, Actor a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
