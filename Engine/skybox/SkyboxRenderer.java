package skybox;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13;

import actors.Camera;
import renderer.Renderer;
import renderer.WorldRenderer;

public class SkyboxRenderer implements Renderer {
	
	private SkyboxShader shader = new SkyboxShader();
	
	public void setProjection(Camera camera) {
		shader.start();
		shader.setProjection(camera.getProjection());
		shader.stop();
	}
	
	public void render(Skybox present, Skybox future, float blendFactor, Camera camera) {
		GL11.glDepthMask(false);
		//GL11.glDepthRange(1, 1);
		shader.start();
		//shader.setProjection(camera.getProjection());
		shader.setView(camera.getTransformation());
		shader.setFog(WorldRenderer.skyColor);
		present.getModel().getVao().bindAttributes(0);
		shader.loadTextures();
		present.getTexture().bind();
		if(future != null) {
			future.getTexture().bind(1);
		}
		shader.setBlendFactor(blendFactor);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, present.getModel().getVao().getIndexCount());//index count will substitute for vertex count here
		present.getModel().getVao().unbindAttributes(0);
		shader.stop();
		//GL11.glDepthRange(0, 1);
		GL11.glDepthMask(true);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
