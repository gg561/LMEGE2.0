package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import actors.Camera;
import actors.Light;
import baseComponents.Vao;
import models.TexturedModel;
import renderer.BaseRenderer;
import textures.Texture;

public class EntityRenderer {
	
	private EntityShader shader;
	private HashMap<TexturedModel, List<Entity>> batches = new HashMap<TexturedModel, List<Entity>>();
	
	public EntityRenderer() {
		this.shader = new EntityShader();
	}
	
	public void render(Camera cam, Light light, boolean doFog) {
		prepare(cam, light);
		for(TexturedModel texture : batches.keySet()) {
			prepareSkin(texture);
			if(texture.getTexture().isHasTransparency()) {
				BaseRenderer.disableCulling();
			}
			for(Entity ent : batches.get(texture)) {
				if(doFog) {
					renderFog(BaseRenderer.GRADIENT, BaseRenderer.DENSITY, BaseRenderer.skyColor);
				}
				if(ent.getModel().getModel().shouldRender()) {
					prepareInstance(ent);
					GL11.glDrawElements(GL11.GL_TRIANGLES, ent.getModel().getModel().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
			}
			unbindSkin(texture);
			BaseRenderer.enableCulling();
		}
		finish();
	}
	
	public void renderFog(float gradient, float density, Vector3f sky) {
		shader.setFog(gradient, density, sky);
	}
	
	private void prepare(Camera camera, Light light) {
		shader.start();
		shader.setCamera(camera.getProjection(), camera.getTransformation());
		shader.setLight(light);
	}
	
	private void prepareInstance(Entity ent) {
		shader.loadModel(ent.getTransformation());
		shader.loadTextures(Arrays.asList(new Texture[] {ent.getModel().getTexture()}));
	}
	
	private void unbindSkin(TexturedModel texture) {
		texture.getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareSkin(TexturedModel texture) {
		shader.loadShineVariables(texture.getTexture().getShineDamper(), texture.getTexture().getReflectivity());
		shader.setUseFakeLighting(texture.isUseFakeLighting());
		texture.getTexture().bind();
		Vao model = texture.getModel().getVao();
		model.bindAttributes(0, 1, 2);
	}
	
	private void prepareBatch(Entity entity) {
		if(batches.containsKey(entity.getModel())) {
			batches.get(entity.getModel()).add(entity);
		}else {
			batches.put(entity.getModel(), new ArrayList<Entity>() {{add(entity);}});
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
