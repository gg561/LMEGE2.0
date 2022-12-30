package game;

public class Settings {
	
	private static boolean physics = false;
	private static boolean collision = true;
	private static boolean scientific = false;
	private static byte quality = 0;
	private static boolean full_screen = false;
	private static int loading_range = 1;
	
	private static boolean accessible = true;
	
	private int id = 0;
	
	public Settings() {
		this(1);
	}
	
	private Settings(int id) {
		if(!accessible) throw new RuntimeException("Settings is not accessible at the Moment");
		this.id = this.id + id;
		accessible = false;
	}
	
	public void setPhysics(boolean toggle) {
		physics = toggle;
	}
	
	public void setCollision(boolean toggle) {
		collision = toggle;
	}
	
	public void setScientific(boolean toggle) {
		scientific = toggle;
	}
	public void setQuality(byte num) {
		quality = num;
	}
	
	public void setFullScreen(boolean toggle) {
		full_screen = toggle;
	}
	
	public void setLoadingRange(int range) {
		loading_range = range;
	}
	
	public static boolean isPhysics() {
		return physics;
	}

	public static boolean isCollision() {
		return collision;
	}

	public static boolean isScientific() {
		return scientific;
	}

	public static byte getQuality() {
		return quality;
	}

	public static boolean isFull_screen() {
		return full_screen;
	}

	public static int getLoading_range() {
		return loading_range;
	}

	public static boolean isAccessible() {
		return accessible;
	}

	public void end() {
		accessible = true;
	}

}
