package game.actors;

public class TileActor extends Actor{
	public TileActor() {
		super(0,0,0,0, -1);
	}
	@Override
	public void update(long delta) {}
	@Override
	public void resolve_collision(long delta, Actor a) {}
	@Override
	public int getType() {
		return Actor.TILE;
	}
}