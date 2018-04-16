package game;

import java.util.ArrayList;

public class Input {
	private boolean[] 			skills;
	private Vec2				skillTarget;
	private ArrayList<Vec2>		mouse;
	private Vec2 				mouse_prev;
	
	public Input() {
		skills = new boolean[Constants.SKILL_NUMBER];
		mouse = null;
	}
	
	public void activateSkill(byte skillIndex) {
		skills[skillIndex] = true;
	}
	
	public void setSkillTarget(Vec2 target) {
		skillTarget = target;
	}
	
	public byte getActiveSkill() {
		for(byte i = 0; i < Constants.SKILL_NUMBER; i++) {
			if (skills[i])
				return i;
		}
		return -1;
	}
	
	public void releaseAll() {
		for (int i = 0; i < Constants.SKILL_NUMBER; i++)
			skills[i] = false;
	}
	
	public boolean isSkillActivated(byte skillIndex) {
		return skills[skillIndex];
	}
	
	public Vec2 getSkillTarget() {return skillTarget;}
	
	public Vec2 getMouse() {
		if (mouse == null) {
			return null;
		}
		if (mouse.size() > 0) {
			return mouse.get(mouse.size() - 1);
		} else {
			return null;
		}
	}
	public void getNextTarget() {
		if (mouse.size() > 0)
			mouse_prev = mouse.remove(mouse.size() - 1);
	}
	
	public void putBackTarget() {
		mouse.add(mouse_prev);
		mouse_prev = null;
	}
	
	public Vec2 getPrev() { return mouse_prev; }
	public void setMouse(ArrayList<TileNode> sequence) {
		for (TileNode i : sequence) {
			mouse.add(new Vec2(i.getX(), i.getY()));
		}
	}
	
	public void setDestination(int x, int y) {
		mouse = new ArrayList<Vec2>();
		mouse.add(new Vec2(x, y));
	}
	
	public void clrMouse() {mouse = null; mouse_prev = null;}
}
