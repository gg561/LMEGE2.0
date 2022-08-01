package generator;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.joml.Vector3f;

import entity.Entity;
import terrain.Terrain;

public class Populator {
	
	public static Entity[] randomPopulate(Entity model, int amount) {
		Entity[] entities = new Entity[amount];
		for(int i = 0; i < amount; i++) {
			Entity house = new Entity(model);
			ThreadLocalRandom rnd = ThreadLocalRandom.current();
			int x = rnd.nextInt(Terrain.getTerrains().size() / 2 * Terrain.SIZE) - (Terrain.getTerrains().size() / 4 * Terrain.SIZE);
			int y = rnd.nextInt(360);
			int z = rnd.nextInt(Terrain.getTerrains().size() / 2 * Terrain.SIZE) - (Terrain.getTerrains().size() / 4 * Terrain.SIZE);
			house.setPosition(new Vector3f(x, 0, z));
			house.setRotation(new Vector3f(0, (float) Math.toRadians(y), 0));
			entities[i] = house;
		}
		if(Arrays.asList(entities).contains(model)) {
			entities[Arrays.asList(entities).indexOf(model)] = null;
		}
		return entities;
	}

}
