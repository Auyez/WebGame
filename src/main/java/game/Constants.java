package game;

public class Constants {
	public static final int MAX_PLAYERS = 1;
	public static final int	GAME_WIDTH = 900;
	public static final int	GAME_HEIGHT = 1200;
	public static final int GAME_TILE_SIZE = 30;
	
	public static final Vec2[] SPAWN_POINTS = {new Vec2(1,1), new Vec2(38, 1), new Vec2(38,27), new Vec2(1, 27)}; //given in tiles
	//PLAYER
	public static final int PLAYER_SPEED = 200;
	public static final int PLAYER_WIDTH = 20;
	public static final int PLAYER_HEIGHT = 40;
	public static final int PLAYER_LOWER_HEIGHT = 20;
	public static final int PLAYER_HP = 300;
	public static final int PLAYER_DEATH_TIME = 10;

	public static final int	SKILL_NUMBER = 2;
	
	
	//Fireball
	public static final int FIREBALL_DMG = 50;
	public static final int	FIREBALL_SPEED = 400;
	public static final int FIREBALL_RANGE = 500;
	public static final int FIREBALL_COOLDOWN = 1;
	public static final int FIREBALL_SIZE = 12;
	
	//Blink
	public static final int BLINK_COOLDOWN = 3;
	public static final int BLINK_RANGE = 600;
}