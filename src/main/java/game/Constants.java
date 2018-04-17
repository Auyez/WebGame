package game;

public class Constants {
	public static final int MAX_LOBBIES = 4;
	public static final int MAX_PLAYERS = 2;
	public static final int	GAME_WIDTH = 1500;
	public static final int	GAME_HEIGHT = 900;
	public static final int GAME_TILE_SIZE = 30;
	
	public static final Vec2[] SPAWN_POINTS = {new Vec2(47,1), new Vec2(2, 1), new Vec2(47,27), new Vec2(2, 27)}; //given in tiles
	//PLAYER
	public static final int PLAYER_SPEED = 200;
	public static final int PLAYER_WIDTH = 32;
	public static final int PLAYER_HEIGHT = 36;
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