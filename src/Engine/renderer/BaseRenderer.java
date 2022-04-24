package Engine.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import Engine.actors.Camera;
import Engine.actors.Light;
import Engine.entity.Entity;
import Engine.entity.EntityRenderer;
import Engine.models.Model;
import Engine.renderer.RendererSettings.Enabled;
import Engine.terrain.Terrain;
import Engine.terrain.TerrainRenderer;
import Engine.textures.Texture;

public class BaseRenderer {
	
	private EntityRenderer er = new EntityRenderer();
	private TerrainRenderer tr = new TerrainRenderer();
	
	public BaseRenderer() {
		for(Enabled enabled : RendererSettings.Enabled.values()) {
			GL11.glEnable(enabled.getEnabled());
			enabled.getValue().run();
		}
	}
	
	public void render(Camera cam, Light light) {
		prepare();
		er.render(cam, light);
		tr.render(cam, light);
	}
	
	private void prepare() {
		GL11.glClearColor(1, 1, 0, 1);
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
	
	public void prepareBatch(List<Entity> ents, List<Terrain> terrains) {
		er.prepareBatch(ents);
		tr.prepareBatch(terrains);
	}
	
	public void cleanUp() {
		er.cleanUp();
		Texture.cleanUp();
	}

}
