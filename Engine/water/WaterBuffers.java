package water;

import baseComponents.Fbo;
import game.Window;

public class WaterBuffers {
	
	protected static final int REFLECTION_WIDTH = 320;
	protected static final int REFLECTION_HEIGHT = 180;
	
	protected static final int REFRACTION_WIDTH = 1280;
	protected static final int REFRACTION_HEIGHT = 720;
	
	
	private Fbo reflection = new Fbo(REFLECTION_WIDTH, REFLECTION_HEIGHT); public Fbo reflection() {return reflection;}
	private Fbo refraction = new Fbo(REFRACTION_WIDTH, REFRACTION_HEIGHT); public Fbo refraction() {return refraction;}
	
	public WaterBuffers(Window frame) {
		initializeReflection(frame);
		initializeRefraction(frame);
	}
	
	private void initializeReflection(Window frame) {
		reflection.bind();
		reflection.createTextureAttachment();
		reflection.createDepthBufferAttachment();
		Fbo.unbind(frame);
	}
	
	private void initializeRefraction(Window frame) {
		refraction.bind();
		refraction.createTextureAttachment();
		refraction.createDepthTextureAttachment();
		Fbo.unbind(frame);
	}
	
	public void cleanUp() {
		reflection.delete();
		refraction.delete();
	}

}
