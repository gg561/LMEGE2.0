package terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import actors.Camera;
import actors.Light;
import baseComponents.Vao;
import entity.Entity;
import models.TexturedModel;
import renderer.BaseRenderer;

public class TerrainRenderer {
	
	private TerrainShader shader = new TerrainShader();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public TerrainRenderer() {
		
	}
	
	public void render(Camera cam, Light light, boolean doFog) {
		prepare(cam, light);
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			if(doFog) {
				renderFog(BaseRenderer.GRADIENT, BaseRenderer.DENSITY, BaseRenderer.skyColor);
			}
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain(terrain);
		}
		finish();
	}
	
	public void renderFog(float gradient, float density, Vector3f sky) {
		shader.setFog(gradient, density, sky);
	}
	
	public void prepare(Camera cam, Light light) {
		shader.start();
		shader.setCamera(cam.getProjection(), cam.getTransformation());
		shader.setLight(light);
	}
	
	private void finish() {
		shader.stop();
	}
	
	private void unbindTerrain(Terrain terrain) {
		terrain.getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareTerrain(Terrain terrain) {
		terrain.getTextures().bind();
		shader.loadShineVariables(100, 0f);
		shader.tiles.loadFloat(terrain.getTiles());
		Vao model = terrain.getModel().getVao();
		model.bindAttributes(0, 1, 2);
		shader.loadModel(terrain.getTransformation());
		//shader.texture().loadSampler(0);
	}
	
	public void prepareBatch(List<Terrain> terrains) {
		this.terrains.addAll(terrains);
	}

}
