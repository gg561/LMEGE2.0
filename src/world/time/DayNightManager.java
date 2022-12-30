package world.time;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import game.Main;
import skybox.Skybox;
import util.Pair;

public class DayNightManager {
	
	public static final Vector3f DEFAULT_COLOR = new Vector3f();
	
	private Vector3f skyColor;
	
	private List<Pair<Integer, Skybox>> skyTextures = new ArrayList<Pair<Integer, Skybox>>();
	
	public static float getDaylightPercentage() {
		float time = Main.CLOCK.daytime();
		float percentage = (float) ((Math.sin(time/WorldClock.MAX_DAYTIME * 2 * Math.PI) + 1)/2);
		if(percentage > 1) percentage = 1;
		return percentage;
	}

}
