package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import actors.Camera;
import actors.Light;
import baseComponents.Vao;
import entity.Entity;
import entity.EntityShader;
import models.TexturedModel;
import renderer.BaseRenderer;
import textures.Texture;

public class GUIRenderer {
	
	private GUIShader shader = new GUIShader();
	private List<GUI> batches = new ArrayList<GUI>();
	
	public GUIRenderer() {
		
	}
	
	public void render(Camera cam) {
		prepare(cam);
		for(GUI gui : batches) {
			prepareSkin(gui.getModel());
			prepareInstance(gui);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, gui.getModel().getModel().getVao().getIndexCount());//index count subsitutes for the vertex count of the VAO. Since we dont have any indices
			unbindSkin(gui.getModel());
		}
		finish();
	}
	
	private void prepare(Camera camera) {
		shader.start();
		//camera.setOrthographic(100, 100, 100, 100);
		//shader.setCamera(camera);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private void prepareInstance(GUI gui) {
		shader.loadModel(gui.getTransformation());
		shader.loadTexture();
	}
	
	private void unbindSkin(TexturedModel texture) {
		texture.getModel().getVao().unbindAttributes(0);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void prepareSkin(TexturedModel texture) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		texture.getTexture().bind();
		Vao model = texture.getModel().getVao();
		model.bindAttributes(0);
	}
	
	private void prepareBatch(GUI gui) {
		batches.add(gui);
	}
	
	public void prepareBatch(List<GUI> guis) {
		batches.addAll(guis);
	}
	
	private void finish() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
