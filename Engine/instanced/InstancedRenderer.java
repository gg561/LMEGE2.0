package instanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;

import actors.Camera;
import actors.lights.Light;
import baseComponents.Vbo;
import entity.Entity;
import models.Model;
import models.TexturedModel;
import renderer.WorldRenderer;
import util.Matrices;

public class InstancedRenderer {
	
	private InstancedShader shader = new InstancedShader();
	private HashMap<TexturedModel, List<Entity>> batches = new HashMap<TexturedModel, List<Entity>>();
	
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 16;
	private static final float CAM_VIEW_DIST = 500;
	/**
	 * DOES NOT SUPPORT NORMAL MAPPED ENTITIES
	 * @param cam
	 * @param lights
	 */
	public void render(Camera cam, List<Light> lights, boolean doFog, Vector4f clipPlane) {
		if(batches.isEmpty()) return;
		prepare(cam, lights);
		shader.setClippingPlane(clipPlane);
		if(doFog) {
			renderFog(WorldRenderer.GRADIENT, WorldRenderer.DENSITY, WorldRenderer.skyColor);
		}
		for(TexturedModel tmodel : batches.keySet()) {
			Vbo vbo = prepareSkin(tmodel);
			float[] data = new float[batches.get(tmodel).size() * INSTANCE_DATA_LENGTH];
			int pointer = 0;
			for(Entity ent : batches.get(tmodel)) {
				//insertMatrixColumns(data, ent.getTransformation());
				pointer = insertMatrixColumns(data, ent.getTransformation(), pointer);
			}
			vbo.update(data);
			GL40.glDrawElementsInstanced(GL11.GL_TRIANGLES, tmodel.getModel().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0, batches.get(tmodel).size());
			//GL40.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, 4, 5);
			unbindSkin(tmodel);
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
	
	private void prepare(Camera cam, List<Light> lights) {
		shader.start();
		shader.setView(cam.getTransformation());
		shader.setLight(lights);
	}
	
	private Vbo prepareSkin(TexturedModel texture) {
		Model model = texture.getModel();
		Vbo vbo = model.getVao().getDataBuffers().get(0);
		shader.setReflectivity(texture.getTexture().getShineDamper(), texture.getTexture().getReflectivity());
		shader.setUseFakeLighting(texture.isUseFakeLighting());
		shader.setTextureAtlasRows(texture.getTexture().getRows());
		shader.setTextureAtlasOffset(texture.getTexture().calculateOffset(texture.getTextureIndex()));
		texture.getTexture().bind();
		model.getVao().bindAttributesStream(0, model.getVao().getLastAttributeBound());
		return vbo;
	}
	
	private void unbindSkin(TexturedModel texture) {
		Model model = texture.getModel();
		model.getVao().unbindAttributesStream(0, model.getVao().getLastAttributeBound());
	}
	
	private void finish() {
		shader.stop();
	}
	
	public void prepareBatch(Entity entity) {
		if(batches.containsKey(entity.getTexturedModel())) {
 			batches.get(entity.getTexturedModel()).add(entity);
		}else {
			batches.put(entity.getTexturedModel(), new ArrayList<Entity>() {{add(entity);}});
			entity.getTexturedModel().getModel().getVao().bindVao();
			Vbo vbo = new Vbo();
			vbo.bind();
			int lab = entity.getTexturedModel().getModel().getVao().getLastAttributeBound();
			vbo.initiateDataBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
			entity.getTexturedModel().getModel().getVao().createInstanceAttribute(vbo, 1 + lab, 1, 4, INSTANCE_DATA_LENGTH, 0);
			entity.getTexturedModel().getModel().getVao().createInstanceAttribute(vbo, 2 + lab, 1, 4, INSTANCE_DATA_LENGTH, 4);
			entity.getTexturedModel().getModel().getVao().createInstanceAttribute(vbo, 3 + lab, 1, 4, INSTANCE_DATA_LENGTH, 8);
			entity.getTexturedModel().getModel().getVao().createInstanceAttribute(vbo, 4 + lab, 1, 4, INSTANCE_DATA_LENGTH, 12);
			entity.getTexturedModel().getModel().getVao().addDataBuffer(vbo);
			entity.getTexturedModel().getModel().getVao().unbindVao();
		}
	}
	
	public void prepareBatch(List<Entity> ents) {
		for(Entity ent : ents) {
			prepareBatch(ent);
		}
	}
	
	public void emptyBatch() {
		for(TexturedModel tm : batches.keySet()) {
			batches.get(tm).clear();
		}
	}
	
	private void insertMatrixColumns(List<Float> floats, Matrix4f mat4) {
		Vector4f dest = new Vector4f();
		for(int i = 0; i < 4; i++) {
			mat4.getColumn(i, dest);
			Collections.addAll(floats, dest.x(), dest.y(), dest.z(), dest.w());
		}
	}
	
	private int insertMatrixColumns(float[] floats, Matrix4f mat4, int begin) {
		Vector4f dest = new Vector4f();
		for(int i = 0; i < 4; i++) {
			mat4.getColumn(i, dest);
			floats[begin++] = dest.x;
			floats[begin++] = dest.y;
			floats[begin++] = dest.z;
			floats[begin++] = dest.w;
		}
		return begin;
	}
	
	private void insertMatrixColumnsToEmptyFloatArray(float[] data, Matrix4f mat4) {
		Vector4f dest = new Vector4f();
		for(int i = 0; i < 4; i++) {
			mat4.getColumn(i, dest);
			data[i * 4] = dest.x;
			data[i * 4 + 1] = dest.y;
			data[i * 4 + 2] = dest.z;
			data[i * 4 + 3] = dest.w;
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
