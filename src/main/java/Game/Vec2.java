package Game;
public class Vec2 {
	private float 		x,y;
	
	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public Vec2(Vec2 v) {set(v);}
	
	public void set(Vec2 v) {
		x = v.getX();
		y = v.getY();
	}
	public void set(float x, float y) { this.x = x;this.y = y; }
	public void setX(float x) {this.x = x;}
	public void setY(float y) {this.y = y;}
	public float getX() {return x;}
	public float getY() {return y;}
	public double getMagnitude() {return Math.sqrt(x*x+y*y);}
	public void add(Vec2 v) { x += v.getX(); y += v.getY();}
	public float dot(Vec2 v) {return dot(this, v);}
	public void scalar(float c) { x *= c; y *= c;}
	
	public static Vec2 add(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}
	public static float dot(Vec2 v1, Vec2 v2) {
		return v1.getX()*v2.getX() + v1.getY()*v2.getY();
	}
	public String toString() {
		return "X:" + x + " Y:" + y;
	}
}
