package shadows;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import baseComponents.Fbo;
import game.Window;
import textures.Texture;

public class ShadowBuffers {
	
	private static Texture shadowTexture; public static Texture shadowTexture() {return shadowTexture;} public static void shadowTexture(Texture texture) {shadowTexture = texture;}
	
	private Fbo shadowBuffer; public Fbo shadowBuffer() {return shadowBuffer;}
	
	public ShadowBuffers(Window frame, int width, int height) {
		shadowBuffer = new Fbo(width, height);
		GL11.glDrawBuffer(GL11.GL_NONE);
		GL11.glReadBuffer(GL11.GL_NONE);
		initializeBuffer(frame);
	}
	
	private void initializeBuffer(Window frame) {
		shadowBuffer.bind();
		shadowBuffer.createShadowDepthTextureAttachment();
		Fbo.unbind(frame);
	}
	
	public void cleanUp() {
		shadowBuffer.delete();
	}

}
