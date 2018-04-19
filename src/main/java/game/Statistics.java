package game;

import java.util.HashMap;
import java.util.Map;

public class Statistics {
	
	private int damage;
	private Map<Integer, Integer> skills_damage;
	private Map<Integer, Integer> skills_used;
	
	public Statistics() {
		damage = 0;
		skills_damage = new HashMap<Integer, Integer>();
		skills_used = new HashMap<Integer, Integer>();
	}
	
	public void initializeSkill(int id) {
		skills_damage.put(id, 0);
		skills_used.put(id, 0);
	}
	public void damageDone(int dmg) {damage += dmg;}
	public void skillDamage(int id, int dmg) {
		skills_damage.put(id, skills_damage.get(id) + dmg);
	}
	public void skillUsed(int id) {
		skills_used.put(id, skills_used.get(id) + 1);
	}
	public int getDamage() {return damage;}
	
	public int getSkillCount(int id) {
		return skills_used.get(id);
	}
	
	public int getSkillDamage(int id) {
		return skills_damage.get(id);
	}
	
	public String toString() {
		String result = "";
		
		for(int key : skills_damage.keySet()) {
			result += "Damage of " + key + ": " + skills_damage.get(key) + "\n";
			
		}
		
		return result;
	}
}
