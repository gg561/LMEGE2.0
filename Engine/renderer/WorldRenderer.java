package renderer;

import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import actors.Camera;
import actors.lights.Light;
import advancedLighting.normalMap.NormalMapRenderer;
import baseComponents.ClippingPlane;
import entity.Entity;
import entity.EntityRenderer;
import game.Main;
import game.Window;
import grass.GrassRenderer;
import gui.GUI;
import gui.GUIRenderer;
import line.Line;
import line.LineRenderer;
import models.Model;
import renderer.RendererSettings.Enabled;
import scene.SceneRenderer;
import shadows.ShadowBuffers;
import shadows.ShadowRenderer;
import skybox.Skybox;
import skybox.SkyboxRenderer;
import terrain.Terrain;
import terrain.TerrainRenderer;
import textures.Texture;
import textures.TextureStaticAssets;
import water.WaterBuffers;
import water.WaterRenderer;
import water.WaterTile;
import world.time.DayNightManager;

public class WorldRenderer implements Renderer {
	
	public static final float GRADIENT = 1.9f;//1.9f
	public static final float DENSITY = 0.007f;//0.007f
	public static Vector3f skyColor = new Vector3f(0.66f, 0.78f, 0.8f); //new Vector3f(2.9f, 0.5f, 0.4f);
	private static float waterHeight = -5;
	
	private SceneRenderer scr; public SceneRenderer getSceneRenderer() {return scr;}
	private GUIRenderer gr;
	private LineRenderer lr;
	private WaterRenderer wr;
	private GrassRenderer gar; public GrassRenderer getGrassRenderer() {return gar;}
	private Window window;
	
	public WorldRenderer() {
		for(Enabled enabled : RendererSettings.Enabled.values()) {
			GL11.glEnable(enabled.getEnabled());
			enabled.getValue().run();
		}
		//skyColor = new Vector3f(0.8f, 0.9f, 1.8f);
	}
	
	public WorldRenderer(Window window, SceneRenderer scr, GUIRenderer gr, LineRenderer lr, WaterRenderer wr, GrassRenderer gar) {
		this();
		this.window = window;
		this.scr = scr;
		this.gr = gr;
		this.lr = lr;
		this.wr = wr;
		this.gar = gar;
	}
	
	public void render(Camera camera, List<Light> lights, Skybox present, Skybox future, float pfBlendFactor) {
		scr.render(camera, lights, present, future, pfBlendFactor, ClippingPlane.NO_CLIP_DISTANCE);
		uniformRender(camera, lights);
	}
	
	public void render(Camera camera, List<Light> lights, Skybox present, Skybox future) {
		scr.render(camera, lights, present, future, ClippingPlane.NO_CLIP_DISTANCE);
		uniformRender(camera, lights);
	}
	
	public void render(Camera camera, List<Light> lights, Skybox present) {
		scr.render(camera, lights, present, null, ClippingPlane.NO_CLIP_DISTANCE);
		uniformRender(camera, lights);
	}
	
	private void uniformRender(Camera camera, List<Light> lights) {
		wr.render(camera, lights, true);
		gar.render(camera);
		gr.render(camera);
		lr.render(camera);
		skyColor = new Vector3f(0.66f, 0.78f, 0.8f).mul(1 - DayNightManager.getDaylightPercentage());
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void setProjection(Camera camera) {
		scr.setProjection(camera);
		lr.setProjection(camera);
		wr.setProjection(camera);
		gar.setProjection(camera);
	}
	
	public void prepareBatch(List<Entity> ents, List<Entity> norms, List<Entity> instances, List<Terrain> terrains, List<WaterTile> tiles, List<GUI> guis, List<Line> lines, List<Entity> bushes) {
		scr.prepareBatch(ents, terrains, norms, instances);
		wr.prepareBatch(tiles);
		gr.prepareBatch(guis);
		lr.prepareBatch(lines);
		gar.prepareBatch(bushes);
	}
	
	public void cleanUp() {
		scr.cleanUp();
		gr.cleanUp();
		lr.cleanUp();
		wr.cleanUp();
		Texture.cleanUp();
	}

}
