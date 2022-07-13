package terrain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import collision.TerrainCollider;
import models.Model;
import renderer.Loader;
import scene.Renderable;
import textures.Texture;
import textures.TexturePack;
import util.Maths;

public abstract class Terrain implements Renderable {
	
	private static HashMap<HashMap<Integer, Integer>, Terrain> terrains = new HashMap<HashMap<Integer, Integer>, Terrain>();
	
	protected static final int SIZE = 800;
	protected static final int VERTEX_COUNT = 128;
	protected static final int TILES = 80;
	
	protected float[][]heights = new float[VERTEX_COUNT][VERTEX_COUNT];
	
	private Vector2f position;
	private Model model;
	private TexturePack texturePack;
	private TerrainCollider collider;
	
	@SuppressWarnings("serial")
	public Terrain(int gridX, int gridZ, Loader loader, TexturePack texturePack) {
		this.texturePack = texturePack;
		this.position = new Vector2f(gridX * SIZE, gridZ * SIZE);
		this.model = generateTerrain(loader);
		terrains.put(new HashMap<Integer, Integer>(){{put(gridX, gridZ);}},  this);
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(new Vector3f(position.x, 0, position.y));
		returnValue.scale(1);
		return returnValue;
	}
	
	public int getTiles() {
		return TILES;
	}
	
	protected abstract Model generateTerrain(Loader loader);

	public float[][] getHeights() {
		return heights;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Model getModel() {
		return model;
	}

	public TexturePack getTextures() {
		return texturePack;
	}
	
	public static HashMap<HashMap<Integer, Integer>, Terrain> getTerrains(){
		return terrains;
	}
	
	@SuppressWarnings("serial")
	public static Terrain getTerrain(float x, float z) {
		return terrains.get(new HashMap<Integer, Integer>(){{put((int) Math.floor(x / SIZE), (int) Math.floor(z / SIZE));}});
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.position.x;//translates world coordinate to terrain coordinate -> world pos - (terrain.x * SIZE);
		float terrainZ = worldZ - this.position.y;
		float gridSquareSize = SIZE / (heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if(xCoord <= (1 - zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}
	
	public void setTerrainCollider(TerrainCollider collider) {
		this.collider = collider;
	}
	
	public TerrainCollider getCollider() {
		return collider;
	}

}
