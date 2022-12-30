package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import actors.Camera;
import actors.lights.Light;
import baseComponents.Vao;
import models.TexturedModel;
import renderer.Renderer;
import renderer.WorldRenderer;
import shadows.ShadowBuffers;
import textures.Texture;
import util.Matrices;
public class EntityRenderer implements Renderer {
	
	private EntityShader shader;
	private HashMap<TexturedModel, List<Entity>> batches = new HashMap<TexturedModel, List<Entity>>();
	
	public EntityRenderer() {
		this.shader = new EntityShader();
	}
	
	public void render(Camera cam, List<Light> lights, boolean doFog, Vector4f clipPlane) {
		if(batches.isEmpty()) return;
		prepare(cam, lights);
		shader.setClippingPlane(clipPlane);
		if(doFog) {
			renderFog(WorldRenderer.GRADIENT, WorldRenderer.DENSITY, WorldRenderer.skyColor);
		}
		for(TexturedModel texture : batches.keySet()) {
			prepareSkin(texture);
			if(texture.getTexture().isHasTransparency()) {
				WorldRenderer.disableCulling();
			}
			for(Entity ent : batches.get(texture)) {
				if(ent.getModel().shouldRender()) {
					prepareInstance(ent);
					GL11.glDrawElements(GL11.GL_TRIANGLES, ent.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
			}
			unbindSkin(texture);
			WorldRenderer.enableCulling();
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
	
	private void prepare(Camera camera, List<Light> lights) {
		shader.start();
		shader.setView(camera.getTransformation());
		shader.setLight(lights);
		shader.setShadow(Matrices.LIGHT_BIASED_DEFAULT.mul(camera.getOrthographic().mul(lights.get(0).getTransformation().translate(camera.getPosition().negate(new Vector3f())), new Matrix4f()), new Matrix4f()));
	}
	
	private void prepareInstance(Entity ent) {
		shader.setModel(ent.getTransformation());
		//shader.loadTextures(new ArrayList<Texture>() {{add(ent.getModel().getTexture()); addAll(additionalTextures);}});
	}
	
	private void unbindSkin(TexturedModel texture) {
		texture.getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareSkin(TexturedModel texture) {
		shader.setReflectivity(texture.getTexture().getShineDamper(), texture.getTexture().getReflectivity());
		shader.setUseFakeLighting(texture.isUseFakeLighting());
		shader.setTextureAtlasRows(texture.getTexture().getRows());
		shader.setTextureAtlasOffset(texture.getTexture().calculateOffset(texture.getTextureIndex()));
		texture.getTexture().bind();
		ShadowBuffers.shadowTexture().bind(1);
		Vao model = texture.getModel().getVao();
		model.bindAttributes(0, 1, 2);
	}
	
	public void prepareBatch(Entity entity) {
		if(batches.containsKey(entity.getTexturedModel())) {
			batches.get(entity.getTexturedModel()).add(entity);
		}else {
			batches.put(entity.getTexturedModel(), new ArrayList<Entity>() {{add(entity);}});
		}
	}
	
	public void prepareBatch(List<Entity> ents) {
		for(Entity ent : ents) {
			prepareBatch(ent);
		}
	}
	
	private void finish() {
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
