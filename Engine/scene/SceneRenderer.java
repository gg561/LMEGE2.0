package scene;

import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21C;
import org.lwjgl.opengl.GL30;

import actors.Camera;
import actors.lights.Light;
import advancedLighting.normalMap.NormalMapRenderer;
import baseComponents.ClippingPlane;
import entity.Entity;
import entity.EntityRenderer;
import instanced.InstancedRenderer;
import renderer.Renderer;
import renderer.WorldRenderer;
import skybox.Skybox;
import skybox.SkyboxRenderer;
import terrain.Terrain;
import terrain.TerrainRenderer;
import water.WaterRenderer;
import water.WaterTile;
import world.time.DayNightManager;

public class SceneRenderer implements Renderer {

	private EntityRenderer er = new EntityRenderer(); public EntityRenderer getEntityRenderer() {return er;}
	private TerrainRenderer tr = new TerrainRenderer(); public TerrainRenderer getTerrainRenderer() {return tr;}
	private SkyboxRenderer sr = new SkyboxRenderer(); public SkyboxRenderer getSkyboxRnderer() {return sr;}
	private NormalMapRenderer nr = new NormalMapRenderer(); public NormalMapRenderer getNormalMapRenderer() {return nr;}
	private InstancedRenderer ir = new InstancedRenderer(); public InstancedRenderer getInstancedRenderer() {return ir;}
	
	public void render(Camera cam, List<Light> lights, Skybox present, Skybox future, float pfBlendFactor, Vector4f clipPlane) {
		uniformRender(cam, lights, clipPlane);
		sr.render(present, future, pfBlendFactor, cam);
	}
	
	public void render(Camera cam, List<Light> lights, Skybox present, Skybox future, Vector4f clipPlane) {
		render(cam, lights, present, future, DayNightManager.getDaylightPercentage(), clipPlane);
	}
	
	public void render(Camera cam, List<Light> lights, Skybox present, Vector4f clipPlane) {
		render(cam, lights, present, null, DayNightManager.getDaylightPercentage(), clipPlane);
	}
	
	private void uniformRender(Camera cam, List<Light> lights, Vector4f clipPlane) {
		prepare();
		GL11.glEnable(GL30.GL_FRAMEBUFFER_SRGB);
		er.render(cam, lights, true, clipPlane);
		nr.render(cam, lights, true, clipPlane);
		tr.render(cam, lights, true, clipPlane);
		ir.render(cam, lights, true, clipPlane);
		GL11.glDisable(GL30.GL_FRAMEBUFFER_SRGB);
	}
	
	public void prepareBatch(List<Entity> ents, List<Terrain> terrains, List<Entity> norms, List<Entity> instances) {
		er.prepareBatch(ents);
		tr.prepareBatch(terrains);
		nr.prepareBatch(norms);
		ir.prepareBatch(instances);
	}
	
	public void setProjection(Camera camera) {
		er.setProjection(camera);
		tr.setProjection(camera);
		sr.setProjection(camera);
		nr.setProjection(camera);
		ir.setProjection(camera);
	}
	
	private void prepare() {
		GL11.glClearColor(WorldRenderer.skyColor.x, WorldRenderer.skyColor.y, WorldRenderer.skyColor.z, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanUp() {
		er.cleanUp();
		sr.cleanUp();
		tr.cleanUp();
		nr.cleanUp();
		ir.cleanUp();
	}
}
