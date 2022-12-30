package terrain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import actors.Actor;
import baseComponents.Vao;
import collision.TerrainCollider;
import models.Model;
import models.TexturedModel;
import renderer.Loader;
import scene.Renderable;
import textures.Texture;
import textures.TexturePack;
import util.Maths;

public abstract class Terrain implements Actor {
	
	private static HashMap<HashMap<Integer, Integer>, Terrain> terrains = new HashMap<HashMap<Integer, Integer>, Terrain>();
	
	public static final int SIZE = 800;//remember to cast to float to use float division instead of int division
	protected static final int VERTEX_COUNT = 128;
	protected static final int TILES = 80;
	
	private Vector2f position;
	private HeightMap heightMap;
	private TexturePack texturePack;
	private TerrainCollider collider;
	
	@SuppressWarnings("serial")
	public Terrain(int gridX, int gridZ, Loader loader, TexturePack texturePack) {
		this.texturePack = texturePack;
		this.position = new Vector2f(gridX * SIZE, gridZ * SIZE);
		this.heightMap = new HeightMap(VERTEX_COUNT);
		getTerrainDataFromHeightMap(loader, this.heightMap);
		this.heightMap.getModel().shouldRender(true);
		terrains.put(new HashMap<Integer, Integer>(){{put(gridX, gridZ);}},  this);
	}
	
	@SuppressWarnings("serial")
	public Terrain(int gridX, int gridZ, Loader loader, TexturePack texturePack, HeightMap heightMap) {
		this.texturePack = texturePack;
		this.position = new Vector2f(gridX * SIZE, gridZ * SIZE);
		this.heightMap = heightMap;
		this.heightMap.getModel().shouldRender(true);
		terrains.put(new HashMap<Integer, Integer>(){{put(gridX, gridZ);}},  this);
	}
	
	protected abstract void getTerrainDataFromHeightMap(Loader loader, HeightMap map);
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(new Vector3f(position.x, 0, position.y));
		returnValue.scale(1);
		return returnValue;
	}
	
	public int getTiles() {
		return TILES;
	}

	public Vector2f getPosition() {
		return position;
	}

	public HeightMap getHeightMap() {
		return heightMap;
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
		return this.heightMap.getHeightAt(terrainX, terrainZ, SIZE);
	}
	
	public void setTerrainCollider(TerrainCollider collider) {
		this.collider = collider;
	}
	
	public TerrainCollider getCollider() {
		return collider;
	}
	
	public TexturedModel getTexturedModel() {
		throw new RuntimeException("This method is not supported");
	}
	
	public Model getModel() {
		return this.heightMap.getModel();
	}
	
	public Vao getVao() {
		return this.getModel().getVao();
	}

}
