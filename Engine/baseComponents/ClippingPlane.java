package baseComponents;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

public class ClippingPlane {
	
	public static final Vector4f NO_CLIP_DISTANCE = new Vector4f(0);
	
	private Vector4f plane; public Vector4f plane() {return plane;}
	
	public ClippingPlane(Vector4f plane) {
		this.plane = plane;
	}
	
	public void apply() {
		apply(0);
	}
	
	public void apply(int unit) {
		GL30.glEnable(GL30.GL_CLIP_DISTANCE0 + unit);
	}
	
	public void cease() {
		cease(0);
	}
	
	public void cease(int unit) {
		GL30.glDisable(GL30.GL_CLIP_DISTANCE0 + unit);
	}

}
