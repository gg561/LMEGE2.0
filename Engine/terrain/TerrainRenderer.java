package terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;

import actors.Camera;
import actors.lights.Light;
import baseComponents.Vao;
import entity.Entity;
import grass.GrassShader;
import models.TexturedModel;
import renderer.Renderer;
import renderer.WorldRenderer;
import shadows.ShadowBuffers;
import shadows.ShadowRenderer;
import util.Matrices;

public class TerrainRenderer implements Renderer {
	
	private TerrainShader shader = new TerrainShader();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public TerrainRenderer() {
		
	}
	
	public void render(Camera cam, List<Light> lights, boolean doFog, Vector4f clipPlane) {
		if(terrains.isEmpty()) return;
		prepare(cam, lights);
		shader.setClippingPlane(clipPlane);
		if(doFog) {
			renderFog(WorldRenderer.GRADIENT, WorldRenderer.DENSITY, WorldRenderer.skyColor);
		}
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			if(terrain.getModel().shouldRender())
				GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain(terrain);
		}
		finish();
	}
	
	public void renderFog(float gradient, float density, Vector3f sky) {
		shader.setFog(gradient, density, sky);
	}
	
	public void setProjection(Camera camera) {
		shader.start();
		shader.setProjection(camera.getProjection());
		shader.stop();
	}
	
	public void prepare(Camera camera, List<Light> lights) {
		shader.start();
		//shader.setProjection(camera.getProjection());
		shader.setView(camera.getTransformation());
		shader.setLight(lights);
		shader.setShadow(Matrices.LIGHT_BIASED_DEFAULT.mul(camera.getOrthographic().mul(lights.get(0).getTransformation().translate(camera.getPosition().negate(new Vector3f())), new Matrix4f()), new Matrix4f()));
	}
	
	private void finish() {
		shader.stop();
	}
	
	private void unbindTerrain(Terrain terrain) {
		terrain.getHeightMap().getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareTerrain(Terrain terrain) {
		terrain.getTextures().bind();
		shader.setReflectivity(10, 0.1f);
		shader.loadTiles(terrain.getTiles());
		Vao model = terrain.getHeightMap().getModel().getVao();
		shader.loadTexture();
		ShadowBuffers.shadowTexture().bind(5); // TO CHANGE TO ADDITIONAL TEXTURE STYLE
		model.bindAttributes(0, 1, 2);
		shader.setModel(terrain.getTransformation());
		//shader.texture().loadSampler(0);
	}
	
	public void prepareBatch(List<Terrain> terrains) {
		this.terrains.addAll(terrains);
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrains.clear();
	}

}
