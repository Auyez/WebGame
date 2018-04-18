package game;

public class Statistics {
	
	private int damage;
	
	public Statistics() {
		damage = 0;
	}
	
	public void damageDone(int dmg) {damage += dmg;}
	
	public int getDamage() {return damage;}
}
