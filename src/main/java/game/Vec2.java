package game;
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
	public float getMagnitude() {return (float)Math.sqrt(x*x+y*y);}
	public float getAngleRad() {
	    float angle = (float)Math.atan2(y, x);
	    if (angle < 0)
	        angle += 2 * Math.PI;
        return angle;
    }
	
	
	public void add(Vec2 v) { x += v.getX(); y += v.getY();}
	public float dot(Vec2 v) {return dot(this, v);}
	public void scalar(float c) { x *= c; y *= c;}
	public static Vec2 subs(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}
	public static Vec2 add(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}
	public static float dot(Vec2 v1, Vec2 v2) {
		return v1.getX()*v2.getX() + v1.getY()*v2.getY();
	}
	public boolean equals(Vec2 v) {
		return v.x == x && v.y == y;
	}
	public boolean isClose(Vec2 v, float range) {
		return ( (	(x - range) <= v.x && v.x <= (x + range) )
			&& (	(y - range) <= v.y && v.y <= (y + range) )	);
	}
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}
