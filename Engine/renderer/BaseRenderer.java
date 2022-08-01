package renderer;

import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import actors.Camera;
import actors.Light;
import entity.Entity;
import entity.EntityRenderer;
import gui.GUI;
import gui.GUIRenderer;
import line.Line;
import line.LineRenderer;
import models.Model;
import renderer.RendererSettings.Enabled;
import terrain.Terrain;
import terrain.TerrainRenderer;
import textures.Texture;

public class BaseRenderer {
	
	public static final float GRADIENT = 1.9f;
	public static final float DENSITY = 0.007f;
	public static Vector3f skyColor = new Vector3f(2.9f, 0.5f, 0.4f);
	
	private EntityRenderer er = new EntityRenderer();
	private TerrainRenderer tr = new TerrainRenderer();
	private GUIRenderer gr = new GUIRenderer();
	private LineRenderer lr = new LineRenderer();
	
	public BaseRenderer() {
		for(Enabled enabled : RendererSettings.Enabled.values()) {
			GL11.glEnable(enabled.getEnabled());
			enabled.getValue().run();
		}
		skyColor = new Vector3f(0.8f, 0.9f, 1.8f);
	}
	
	public void render(Camera cam, List<Light> lights) {
		prepare();
		er.render(cam, lights, true);
		tr.render(cam, lights, true);
		gr.render(cam);
		lr.render(cam);
	}
	
	private void prepare() {
		GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void prepareBatch(List<Entity> ents, List<Terrain> terrains, List<GUI> guis, List<Line> lines) {
		er.prepareBatch(ents);
		tr.prepareBatch(terrains);
		gr.prepareBatch(guis);
		lr.prepareBatch(lines);
	}
	
	public void cleanUp() {
		er.cleanUp();
		gr.cleanUp();
		Texture.cleanUp();
	}

}
