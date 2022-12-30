package font;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import actors.Camera;
import baseComponents.Vao;
import gui.GUI;
import models.TexturedModel;
import renderer.Renderer;

public class FontRenderer implements Renderer {
	
	
	private FontShader shader = new FontShader();
	private List<Caractere> batches = new ArrayList<Caractere>();
	
	public FontRenderer() {
		shader.start();
		shader.setProjection(new Matrix4f().identity());
		shader.stop();
	}
	
	public void setProjection(Camera camera) {
		shader.start();
		shader.setProjection(camera.getOrthographic());
		shader.stop();
	}
	
	public void render(Camera cam) {
		if(batches.isEmpty()) return;
		prepare(cam);
		for(Caractere caractere : batches) {
			prepareSkin(caractere.getModel());
			prepareInstance(caractere);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, caractere.getModel().getModel().getVao().getIndexCount());//index count subsitutes for the vertex count of the VAO. Since we dont have any indices
			unbindSkin(caractere.getModel());
		}
		finish();
	}
	
	private void prepare(Camera camera) {
		shader.start();
		//camera.setOrthographic(100, 100, 100, 100);
		//shader.setCamera(camera);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private void prepareInstance(Caractere caractere) {
		shader.setModel(caractere.getTransformation());
		shader.setColor(caractere.getColor());
		//shader.loadTexture();
	}
	
	private void unbindSkin(TexturedModel texture) {
		texture.getModel().getVao().unbindAttributes(0, 1);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void prepareSkin(TexturedModel texture) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		texture.getTexture().bind();
		Vao model = texture.getModel().getVao();
		model.bindAttributes(0, 1);
	}
	
	private void prepareBatch(Caractere caractere) {
		batches.add(caractere);
	}
	
	public void prepareBatch(List<Text> texts) {
		for(Text t : texts) {
			batches.addAll(t.getCaracteres());
		}
	}
	
	private void finish() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}